import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;

/**
 * The type Parser.
 */
public class Parser
{
    private String root;
    private String indexFile;
    private Logger logger;

    /**
     * Instantiates a new Parser.
     *
     * @param logger    the logger
     * @param root      the root
     * @param indexFile the index file
     */
    Parser(Logger logger, String root, String indexFile)
    {
        this.root = root;
        this.indexFile = indexFile;
        this.logger = logger;
    }

    private void logMessage(String message)
    {
        logger.print(String.format("[Parser]: %s", message));
    }

    /**
     * Handle.
     *
     * @param client  the client
     * @param request the request
     */
    public void handle(Client client, Request request)
    {
        logMessage("new request");
        //
        if (request.getMethod().equals(Method.POST))
        {
            try
            {
                postResponse(client, request);
            } catch (IOException e)
            {
                logMessage(e.getMessage());
                client.sendResponse(new StockResponse(Status.Internal_Server_Error));
            }
            return;
        }



        if (request.getMethod().equals(Method.PUT))
        {
            try
            {
                writeBody(request);
                client.sendResponse(new StockResponse(Status.CREATED));
            } catch (IOException e)
            {
                logMessage(e.getMessage());
                client.sendResponse(new StockResponse(Status.Internal_Server_Error));
            }
            return;
        }


        // Authenticated request. If the requested not authenticated in the uri query, forbidden response will be sent 302
        if (request.getUri().getPath().equals("/admin") || request.getUri().getPath().equals("/admin/"))
        {
            if (!request.getUri().getQuery().getValue("username").equals("admin") || !request.getUri().getQuery().getValue("password").equals("admin")

            )
            {
                client.sendResponse(new StockResponse(Status.FORBIDDEN));
                return;
            }
        }
        File file = null;

        if (request.getUri().getPath().equals("/redirect") || request.getUri().getPath().equals("/redirect/"))
        {
            Pairs headers = new Pairs();
            headers.add("Location", "/found/found.html");
            client.sendResponse(new StockResponse(Status.Found, headers));
            return;

//            file = new File("/found/found.html");
//            parseReadFile(client, ContentType.toString(ContentType.fromExtension(getExtension(file.getPath()))), file, Status.Found);
//            return;
        }

        try
        {
            file = getFile(getAbsolutePath(request.getUri().getPath()));

        } catch (SecurityException e)
        {
            client.sendResponse(new StockResponse(Status.FORBIDDEN));
            return;
        } catch (FileSystemNotFoundException | FileNotFoundException e)
        {
            client.sendResponse(new StockResponse(Status.NOT_FOUND));
            return;
        }

        parseReadFile(client, ContentType.toString(ContentType.fromExtension(getExtension(file.getPath()))), file, Status.OK);
    }

    private void parseReadFile(Client client, String contentType, File file, Status status)
    {
        Response response = new StockResponse(status);
        Pairs pairs = new Pairs();
        pairs.add("Content-Length", file.length());
        pairs.add("Content-Type", contentType);
        response.setPairs(pairs);
        response.build();   // build response headers

        // write response body
        try
        {
            response.write(Files.readAllBytes(file.toPath()));
        } catch (IOException e)
        {
            logMessage(e.getMessage());
            client.sendResponse(new StockResponse(Status.Internal_Server_Error)); ////////////////////
            return;
        }
        client.sendResponse(response);
    }

    private String getExtension(String path)
    {
        String extension = "";

        int i = path.lastIndexOf('.');
        if (i > 0)
        {
            extension = path.substring(i + 1);
        }
        return extension;
    }

    private String getAbsolutePath(String relativePath)
    {
        if (relativePath.endsWith("/"))
        {
            return root + relativePath + indexFile;
        }
        if (relativePath.endsWith("/index.htm"))
        {
            return root + "/" + indexFile;
        }
        return root + relativePath;
    }

    private File getFile(String absolutePath) throws FileNotFoundException
    {
        File file = new File(absolutePath);
        if (file.getName().equals("deny.html") && !file.isDirectory() && file.exists())
        {
            throw new SecurityException();
        }
        if (file.exists() && file.isDirectory())
        {
            if (new File(absolutePath + "/index.html").exists()) return new File(absolutePath + "/index.html");
            if (new File(absolutePath + "/index.htm").exists()) return new File(absolutePath + "/index.htm");
            return file;
        }
        if (!file.exists()) throw new FileNotFoundException();
        return file;
    }

    /**
     * Post response.
     *
     * @param client  the client
     * @param request the request
     * @throws IOException the io exception
     */
    public void postResponse(Client client, Request request) throws IOException
    {
        writeBody(request);
        Pairs headers = new Pairs();
        headers.add("Location", request.getUri().getPath());
        client.sendResponse(new StockResponse(Status.MOVED_PERMANENTLY, headers));
    }

    private void writeBody(Request request) throws IOException
    {
        // To test the put method,
        String fileName = request.getUri().getPath();
        Pairs info = new Pairs(request.getBody());
        String contentDisposition = info.getValue("Content-Disposition");

        int fileNamePos = contentDisposition.indexOf("filename=\"");
        if (fileNamePos >= 0)
        {
            int filenameEndPos = contentDisposition.indexOf("\"", fileNamePos + 11);
            if (filenameEndPos >= 0)
            {
                fileName = contentDisposition.substring(fileNamePos + 10, filenameEndPos);
            } else
            {
                fileName = contentDisposition.substring(fileNamePos + 10);
            }
        }


        String postKey = "";
        for (String key : info.getKeys())
        {
            if (key.startsWith("--"))
            {
                postKey = key;
                break;
            }
        }
        int endPos = request.getBody().hasBytes(postKey.getBytes());
        if (endPos > 0)
        {
            writeFile(root + "/" + fileName, request.getBody().get(endPos - 2));
            request.getBody().extract(endPos + postKey.length());
        } else {
            writeFile(root + "/" + fileName, request.getBody().get());
            request.getBody().extract(request.getBody().getSize());
        }
    }

    private Status writeFile(String path, byte [] body) throws IOException
    {
        File file = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        fileOutputStream.write(body);
        fileOutputStream.flush();
        fileOutputStream.close();

        return Status.OK;
    }

}
