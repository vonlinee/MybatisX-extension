package com.baomidou.mybatisx.plugin.extensions;

/**
 * The interface List selection listener.
 *
 * @author yanglin
 */
public interface ListSelectionListener extends ExecutableListener {

    /**
     * Selected.
     *
     * @param index the index
     */
    void selected(int index);
}
