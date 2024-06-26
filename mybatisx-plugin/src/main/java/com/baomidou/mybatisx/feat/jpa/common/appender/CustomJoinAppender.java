package com.baomidou.mybatisx.feat.jpa.common.appender;


import com.baomidou.mybatisx.feat.jpa.SyntaxAppenderWrapper;
import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;
import com.baomidou.mybatisx.feat.jpa.common.command.AppendTypeCommand;
import com.baomidou.mybatisx.feat.jpa.common.command.JoinAppendTypeCommand;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.baomidou.mybatisx.feat.jpa.operate.model.AppendTypeEnum;
import com.intellij.psi.PsiClass;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The type Custom join appender.
 */
public class CustomJoinAppender implements SyntaxAppender {

    /**
     * The constant SPACE.
     */
    public static final String SPACE = " ";
    private static final Logger logger = LoggerFactory.getLogger(CustomJoinAppender.class);
    private final String tipText;
    private String sqlText;
    private AreaSequence areaSequence;

    /**
     * Instantiates a new Custom join appender.
     *
     * @param tipText      the tip text
     * @param sqlText      the sql text
     * @param areaSequence the area sequence
     */
    public CustomJoinAppender(String tipText, String sqlText, AreaSequence areaSequence) {
        this.tipText = tipText;
        this.sqlText = sqlText;
        this.areaSequence = areaSequence;
    }

    @Override
    public AreaSequence getAreaSequence() {
        return areaSequence;
    }

    @Override
    public String getText() {
        return this.tipText;
    }

    @Override
    public AppendTypeEnum getType() {
        return AppendTypeEnum.JOIN;
    }

    @Override
    public List<AppendTypeCommand> getCommand(String areaPrefix, List<SyntaxAppender> splitList) {
        return Collections.singletonList(new JoinAppendTypeCommand(this));
    }

    @Override
    public Optional<SyntaxAppender> pollLast(LinkedList<SyntaxAppender> splitList) {
        final Optional<SyntaxAppender> syntaxAppender = Optional.ofNullable(splitList.pollLast());
        if (!splitList.isEmpty()) {
            final SyntaxAppender last = splitList.getLast();
            return last.pollLast(splitList);
        }
        return syntaxAppender;
    }

    @Override
    public boolean getCandidateAppender(LinkedList<SyntaxAppender> result) {
        if (result.isEmpty()) {
            result.add(this);
            return true;
        }
        return false;
    }

    @Override
    public String getTemplateText(String tableName, PsiClass entityClass, LinkedList<TxParameter> parameters, LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
        return sqlText + SPACE;
    }

    @Override
    public List<TxParameter> getMxParameter(LinkedList<SyntaxAppenderWrapper> syntaxAppenderWrapperLinkedList, PsiClass entityClass) {
        return Collections.emptyList();
    }

    @Override
    public void toTree(LinkedList<SyntaxAppender> jpaStringList, SyntaxAppenderWrapper syntaxAppenderWrapper) {
        syntaxAppenderWrapper.addWrapper(new SyntaxAppenderWrapper(this));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("tipText", tipText)
            .append("sqlText", sqlText)
            .append("areaSequence", areaSequence)
            .toString();
    }

    @Override
    public boolean checkAfter(SyntaxAppender secondAppender, AreaSequence areaSequence) {
        return getAreaSequence().getSequence() == secondAppender.getAreaSequence().getSequence();
    }
}
