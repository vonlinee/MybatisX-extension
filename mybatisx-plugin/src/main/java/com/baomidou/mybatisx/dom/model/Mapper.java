package com.baomidou.mybatisx.dom.model;

import com.baomidou.mybatisx.dom.converter.NamespaceConverter;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <p>
 * Mapper 元素接口
 * </p>
 *
 * @author yanglin jobob
 * @since 2018 -07-30
 */
@Namespace("MybatisXml")
public interface Mapper extends DomElement {

    /**
     * Gets dao elements.
     *
     * @return the dao elements
     */
    @NotNull
    @SubTagsList({"insert", "update", "delete", "select"})
    List<IdDomElement> getDaoElements();

    /**
     * Gets namespace.
     *
     * @return the namespace
     */
    @Required
    @NameValue
    @Convert(NamespaceConverter.class)
    @NotNull
    @Attribute("namespace")
    GenericAttributeValue<PsiClass> getNamespace();

    /**
     * Gets result maps.
     *
     * @return the result maps
     */
    @NotNull
    @SubTagList("resultMap")
    List<ResultMap> getResultMaps();

    /**
     * Gets parameter maps.
     *
     * @return the parameter maps
     */
    @NotNull
    @SubTagList("parameterMap")
    List<ParameterMap> getParameterMaps();

    /**
     * Gets sqls.
     *
     * @return the sqls
     */
    @NotNull
    @SubTagList("sql")
    List<Sql> getSqlFragments();

    /**
     * Gets inserts.
     *
     * @return the inserts
     */
    @NotNull
    @SubTagList("insert")
    List<Insert> getInserts();

    /**
     * Gets updates.
     *
     * @return the updates
     */
    @NotNull
    @SubTagList("update")
    List<Update> getUpdates();

    /**
     * Gets deletes.
     *
     * @return the deletes
     */
    @NotNull
    @SubTagList("delete")
    List<Delete> getDeletes();

    /**
     * Gets selects.
     *
     * @return the selects
     */
    @NotNull
    @SubTagList("select")
    List<Select> getSelects();

    /**
     * Add select.
     *
     * @return the select
     */
    @SubTagList("select")
    Select addSelect();

    /**
     * Add update update.
     *
     * @return the update
     */
    @SubTagList("update")
    Update addUpdate();

    /**
     * Add insert insert.
     *
     * @return the insert
     */
    @SubTagList("insert")
    Insert addInsert();

    /**
     * Add delete delete.
     *
     * @return to delete
     */
    @SubTagList("delete")
    Delete addDelete();
}
