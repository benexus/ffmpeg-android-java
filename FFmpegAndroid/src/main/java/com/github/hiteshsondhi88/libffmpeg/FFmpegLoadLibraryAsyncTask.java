package com.github.hiteshsondhi88.libffmpeg;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;

class FFmpegLoadLibraryAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final String cpuArchNameFromAssets;
    private final FFmpegLoadBinaryResponseHandler ffmpegLoadBinaryResponseHandler;
    private final Context context;
    private final String url;

    FFmpegLoadLibraryAsyncTask(Context context, String cpuArchNameFromAssets, FFmpegLoadBinaryResponseHandler ffmpegLoadBinaryResponseHandler, String url) {
        this.context = context;
        this.cpuArchNameFromAssets = cpuArchNameFromAssets;
        this.ffmpegLoadBinaryResponseHandler = ffmpegLoadBinaryResponseHandler;
        this.url = url;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        File ffmpegFile = new File(FileUtils.getFFmpeg(context));
        if (ffmpegFile.exists() && isDeviceFFmpegVersionOld() && !ffmpegFile.delete()) {
            return false;
        }
        if (!ffmpegFile.exists()) {
            boolean isFileCopied = FileUtils.copyBinaryFromAssetsToData(context,
                    cpuArchNameFromAssets + File.separator + FileUtils.ffmpegFileName,
                    FileUtils.ffmpegFileName);
            if(!isFileCopied) {
                if(url == null) {
                    Log.e("Can't download binaries because URL was not set. Please call setUrl() on your FFmpeg instance.");
                    return false;
                }
                // Download from url
                String binaryUrl = url + File.separator + cpuArchNameFromAssets + File.separator + FileUtils.ffmpegFileName;
                isFileCopied = FileUtils.copyBinaryFromUrl(context, binaryUrl, FileUtils.ffmpegFileName);
            }

            // make file executable
            if (isFileCopied) {
                if(!ffmpegFile.canExecute()) {
                    Log.d("FFmpeg is not executable, trying to make it executable ...");
                    if (ffmpegFile.setExecutable(true)) {
                        return true;
                    }
                } else {
                    Log.d("FFmpeg is executable");
                    return true;
                }
            }
        }
        return ffmpegFile.exists() && ffmpegFile.canExecute();
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
        if (ffmpegLoadBinaryResponseHandler != null) {
            if (isSuccess) {
                ffmpegLoadBinaryResponseHandler.onSuccess();
            } else {
                ffmpegLoadBinaryResponseHandler.onFailure();
            }
            ffmpegLoadBinaryResponseHandler.onFinish();
        }
    }

    private boolean isDeviceFFmpegVersionOld() {
        // Since binaries are not bundled anymore, there is no point in checking if they are old
        return false;
        // return CpuArch.fromString(FileUtils.SHA1(FileUtils.getFFmpeg(context))).equals(CpuArch.NONE);
    }
}
