package com.dilusense.faceplaydemo.net.utils;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Thinkpad on 2017/3/3.
 */
public class MultipartUtils {
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static MultipartBody filesToMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (File file : files) {
            // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("images", file.getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        if (file != null) {
            // 为file建立RequestBody实例
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
            // MultipartBody.Part借助文件名完成最终的上传
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }
        return null;
    }

    public static RequestBody createPartFromString(String descriptionString) {
        if (descriptionString == null) {
            descriptionString = "";
        }
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

}
