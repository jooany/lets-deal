package com.jooany.letsdeal.util;

import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;

import java.util.UUID;

public class FileUtils {
    public static void checkImageMimeType(String mimeType) {
        if (!(mimeType.equals("image/jpg") || mimeType.equals("image/jpeg")
                || mimeType.equals("image/png") || mimeType.equals("image/gif"))) {
            throw new LetsDealAppException(ErrorCode.INVALID_IMAGE_MIME_TYPE);
        }
    }

    public static String convertFileName(String fileName) {
        StringBuilder builder = new StringBuilder();
        UUID uuid = UUID.randomUUID();
        String extension = getExtension(fileName);
        builder.append(uuid).append(".").append(extension);

        return builder.toString();
    }

    private static String getExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }
}
