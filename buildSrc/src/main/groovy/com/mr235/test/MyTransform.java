package com.mr235.test;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class MyTransform extends Transform {
    @Override
    public String getName() {
        return "MyTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        System.out.println("==================== Plugin ====================MyTransform");
        for (TransformInput input : transformInvocation.getInputs()) {
            for (JarInput jarInput : input.getJarInputs()) {
                // 将 JAR 输入复制到输出
                File dest = transformInvocation.getOutputProvider().getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                FileUtils.copyFile(jarInput.getFile(), dest);
            }

            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                // 将目录输入复制到输出
                File dest = transformInvocation.getOutputProvider().getContentLocation(
                        directoryInput.getFile().getAbsolutePath(),
                        directoryInput.getContentTypes(),
                        directoryInput.getScopes(),
                        Format.DIRECTORY);
                FileUtils.copyDirectory(directoryInput.getFile(), dest);
            }
        }
    }
}
