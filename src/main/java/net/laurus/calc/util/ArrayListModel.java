package net.laurus.calc.util;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

public class ArrayListModel<T> extends AbstractListModel<T> {

	private static final long serialVersionUID = -5367343472901396822L;

	private final ArrayList<T> mInternalBacking;

	public ArrayListModel(ArrayList<T> aList) {
		this.mInternalBacking = aList;
	}

	@Override
	public int getSize() {
		return this.mInternalBacking.size();
	}

	@Override
	public T getElementAt(int index) {
		return this.mInternalBacking.get(index);
	}

}
