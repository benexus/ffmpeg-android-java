package com.github.hiteshsondhi88.libffmpeg;

import android.text.TextUtils;

enum CpuArch {
    x86("8ae30e9f1f9b01f2d8b08f30184c0ecb35a5de0b"),
    ARMv7("98ff9e9de3fe356d7be3824c2a99a43b8d5a52cf"),
    ARMv7_NEON("58a2181ef367bc4fdd214d13af2f0b957ab60dbd"),
    NONE(null);

    private String sha1;

    CpuArch(String sha1) {
        this.sha1 = sha1;
    }

    String getSha1(){
        return sha1;
    }

    static CpuArch fromString(String sha1) {
        if (!TextUtils.isEmpty(sha1)) {
            for (CpuArch cpuArch : CpuArch.values()) {
                if (sha1.equalsIgnoreCase(cpuArch.sha1)) {
                    return cpuArch;
                }
            }
        }
        return NONE;
    }
}
