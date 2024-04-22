package com.xinhua.language.wanbang.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApkDownloadUtils {

    private OkHttpClient okHttpClient;

    private Map<String, Call> callMap = new HashMap<>();

    public ApkDownloadUtils() {
        okHttpClient = new OkHttpClient();
    }

    public void download(final String url, final String saveDir, final String fileName, final OnDownloadListener onDownloadListener) {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        callMap.put(url, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onDownloadListener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        onDownloadListener.onDownloading(progress);
                    }
                    fos.flush();
                    callMap.remove(url);
                    onDownloadListener.onDownloadSuccess(file);
                } catch (Exception e) {
                    onDownloadListener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    private String isExistDir(String saveDir) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File downloadFile = new File(saveDir);
            if (!downloadFile.mkdirs()) {
                downloadFile.createNewFile();
            }
            String savePath = downloadFile.getAbsolutePath();
            Log.e("savePath", savePath);
            return savePath;
        }
        return null;
    }

    public void cancel(String url) {
        Call call = callMap.get(url);
        if (call != null) {
            call.cancel();
            callMap.remove(url);
        }
    }

    public String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        void onDownloadSuccess(File str);

        void onDownloading(int progress);

        void onDownloadFailed();
    }

}
