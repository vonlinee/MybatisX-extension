package com.baomidou.mybatisx.plugin.extensions;

import com.baomidou.mybatisx.util.Icons;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;

/**
 * The type Mybatis file template descriptor factory.
 *
 * @author yanglin
 */
public class MyBatisFileTemplateDescriptorFactory implements FileTemplateGroupDescriptorFactory {

    /**
     * The constant MYBATIS_MAPPER_XML_TEMPLATE.
     */
    public static final String MYBATIS_MAPPER_XML_TEMPLATE = "Mybatis Mapper.xml";

    @Override
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor("Mybatis", Icons.MYBATIS_LOGO);
        group.addTemplate(new FileTemplateDescriptor(MYBATIS_MAPPER_XML_TEMPLATE, Icons.MYBATIS_LOGO));
        return group;
    }
}
