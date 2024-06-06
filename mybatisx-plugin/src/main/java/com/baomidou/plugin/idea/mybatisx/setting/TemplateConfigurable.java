package com.baomidou.plugin.idea.mybatisx.setting;

import com.baomidou.plugin.idea.mybatisx.component.CodeArea;
import com.baomidou.plugin.idea.mybatisx.component.SplitPane;
import com.baomidou.plugin.idea.mybatisx.component.TreeView;
import com.baomidou.plugin.idea.mybatisx.generate.setting.TemplatesSettings;
import com.baomidou.plugin.idea.mybatisx.model.TemplateInfo;
import com.baomidou.plugin.idea.mybatisx.util.CollectionUtils;
import com.baomidou.plugin.idea.mybatisx.util.FileUtils;
import com.baomidou.plugin.idea.mybatisx.util.PluginUtils;
import com.baomidou.plugin.idea.mybatisx.util.SwingUtils;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 模板配置
 */
public class TemplateConfigurable implements SearchableConfigurable {

    JPanel rootPanel;
    TemplatesSettings templatesSettings;

    @Override
    public @NotNull String getId() {
        return getClass().getName();
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return getId();
    }

    @Override
    public @Nullable JComponent createComponent() {

        if (rootPanel == null) {
            JPanel root = new JPanel();
            root.setLayout(new BorderLayout());

            JTabbedPane container = new JTabbedPane();

            SplitPane tabContainer = new SplitPane();
            tabContainer.setProportion(0.4F);
            TreeView<TemplateInfo> treeView = new TreeView<>();
            treeView.setMinimumSize(new Dimension(200, treeView.getHeight()));
            tabContainer.setLeftComponent(treeView);

            Project currentProject = PluginUtils.getCurrentProject();

            List<TemplateInfo> templates = TemplatesSettings.getInstance(currentProject).templates;

            Map<String, TemplateInfo> map = CollectionUtils.toMap(templates, TemplateInfo::getName);

            for (TemplateInfo template : templates) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(template.getName());
                treeView.addChild(node);
            }
            treeView.expandAll();

            CodeArea field = new CodeArea();
            tabContainer.setRightComponent(field);

            field.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {
                    // 将文件变更写入文件

                    DefaultMutableTreeNode node = SwingUtils.getSelectedNode(treeView);
                    TemplateInfo templateInfo = map.get(node.getUserObject());
                    templateInfo.setContent(field.getText());
                }
            });

            treeView.addTreeSelectionListener(e -> {
                // 如果是要相应的那棵树
                if (e.getSource() == treeView) {
                    // 获取当前选择的第一个结点中的最后一个路径组件
                    DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
                    // 如果是叶子结点
                    if (dmt.isLeaf()) {
                        String value = dmt.toString();// 叶子结点的字符串
                        // 判断
                        TemplateInfo templateInfo = map.get(value);
                        if (templateInfo.getContent() == null) {
                            templateInfo.setContent(FileUtils.readString(new File(templateInfo.getPath())));
                        }
                        field.setText(templateInfo.getContent());
                    }
                }
            });

            container.addTab("代码生成", tabContainer);
            root.add(container, BorderLayout.CENTER);

            root.setPreferredSize(new Dimension(500, 400));
            this.rootPanel = root;
        }
        return this.rootPanel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
