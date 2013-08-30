package com.novelbio.nbcgui.GUI;

import com.novelbio.base.gui.GUIFileOpen;

/**
 * 打算把所有界面的GUIFileOpen换成同一个
 * 这样一个GUI打开文件夹后，其他GUI也会直接打开同样的文件夹
 * @author zong0jie
 *
 */
public interface GuiNeedOpenFile {
	public void setGuiFileOpen(GUIFileOpen guiFileOpen);
}
