package com.baomidou.plugin.idea.mybatisx.boot;

import com.baomidou.plugin.idea.mybatisx.model.TemplateInfo;
import com.baomidou.plugin.idea.mybatisx.setting.DataTypeMappingTableModel;
import com.baomidou.plugin.idea.mybatisx.setting.GlobalTemplateSettings;
import com.baomidou.plugin.idea.mybatisx.setting.MyBatisXSettings;
import com.baomidou.plugin.idea.mybatisx.util.FileUtils;
import com.baomidou.plugin.idea.mybatisx.util.IOUtils;
import com.baomidou.plugin.idea.mybatisx.util.PluginUtils;
import com.intellij.ide.AppLifecycleListener;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginInitializer implements AppLifecycleListener {

    private static final Logger logger = LoggerFactory.getLogger(PluginInitializer.class);

    /**
     * 模板存放目录
     */
    private static final String TEMPLATE_DIRECTORY = "extensions/templates/";

    /**
     * 启动时初始化
     */
    @Override
    public void appStarted() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(TEMPLATE_DIRECTORY);
        JarFile jarFile = getPluginJarLocation(url, classLoader);
        if (jarFile == null) {
            logger.error("cannot find jar location of this plugin. stop needed initialization");
            return;
        }
        List<TemplateInfo> templates = new ArrayList<>();
        try {
            Path rootDir = FileUtils.getUserHomePath(PluginUtils.PLUGIN_NAME);
            FileUtils.createDirectoriesQuietly(rootDir);
            /**
             * 将内置模板拷贝到本地目录 {user.home}/{插件名称}/extensions/templates
             */
            final String rootDirString = rootDir.toString();
            Enumeration<JarEntry> jarEntry = jarFile.entries();
            int i = 0;
            while (jarEntry.hasMoreElements()) {
                JarEntry entry = jarEntry.nextElement();
                String name = entry.getName();
                // 仅支持freemarker模板
                if (name.startsWith(TEMPLATE_DIRECTORY) && !entry.isDirectory() && name.endsWith(".ftl")) {
                    Path path = Paths.get(rootDirString, name);
                    if (!Files.exists(path)) {
                        FileUtils.createNewFile(path);
                        try (InputStream in = jarFile.getInputStream(entry);
                             OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)) {
                            IOUtils.copy(in, out);
                            String absolutePathname = FileUtils.getAbsolutePathname(path);
                            String filename = path.getFileName().toString();
                            templates.add(new TemplateInfo(String.valueOf(++i), filename, absolutePathname));
                        }
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("failed to copy templates", exception);
        }

        GlobalTemplateSettings.getInstance().setTemplates(templates);

        MyBatisXSettings settings = MyBatisXSettings.getInstance();

        // 数据类型映射信息
        URL resource = getClass().getClassLoader().getResource("typemapping.ini");
        if (resource != null) {
            try {
                List<Object[]> rows = DataTypeMappingTableModel.readRows(resource.toURI());
                Map<String, Map<String, String>> dataTypeMappingMap = new HashMap<>();
                for (Object[] row : rows) {
                    String sectionName = String.valueOf(row[0]);
                    Map<String, String> map;
                    if (dataTypeMappingMap.containsKey(sectionName)) {
                        map = dataTypeMappingMap.get(sectionName);
                    } else {
                        map = new HashMap<>();
                        dataTypeMappingMap.put(sectionName, map);
                    }
                    map.put(String.valueOf(row[1]), String.valueOf(row[2]));
                }
                settings.getState().dataTypeMapping.putAll(dataTypeMappingMap);
            } catch (URISyntaxException e) {
                logger.error("failed to init data type mapping data", e);
            }
        }
    }

    @Nullable
    private static JarFile getPluginJarLocation(URL url, ClassLoader classLoader) {
        JarFile jarLocation = null;
        try {
            if (url == null) {
                if (classLoader instanceof PluginClassLoader) {
                    PluginClassLoader pcl = (PluginClassLoader) classLoader;
                    List<String> libDirectories = pcl.getLibDirectories();
                    String rootDir = null;
                    for (String libDirectory : libDirectories) {
                        if (libDirectory.contains("mybatisx-plugin")) {
                            rootDir = libDirectory;
                        }
                    }
                    if (rootDir != null) {
                        File[] files = new File(rootDir).listFiles();
                        if (files != null) {
                            for (File file : files) {
                                if (file.getName().endsWith("mybatisx-plugin.jar")) {
                                    jarLocation = new JarFile(file);
                                }
                            }
                        }
                    }
                }
            } else {
                jarLocation = new JarFile(url.toString().substring(0, url.toString().indexOf("!/") + 2));
            }
        } catch (Throwable ignored) {
        }
        return jarLocation;
    }
}
