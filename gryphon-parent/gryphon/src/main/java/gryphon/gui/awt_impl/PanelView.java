package gryphon.gui.awt_impl;

import gryphon.Entity;
import gryphon.common.Descriptor;
import gryphon.gui.awt_impl.AwtView;

import java.awt.Component;
import java.awt.Panel;
import java.awt.event.MouseEvent;

public class PanelView extends Panel implements AwtView
{

	private static final long serialVersionUID = 1L;
	private Descriptor descriptor;
	private Entity entity;

	public void createAndShowPopup(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public Component getComponent() throws Exception
	{
		return this;
	}

	public Descriptor getDescriptor() throws Exception
	{
		return descriptor;
	}

	public Entity getEntity() throws Exception
	{
		return entity;
	}
	/**
	 * Базовая реализация ничего не делает
	 */
	public void init() throws Exception
	{
	}

	public void setDescriptor(Descriptor descriptor) throws Exception
	{
		this.descriptor = descriptor;
	}

	public void setEntity(Entity entity) throws Exception
	{
		this.entity = entity;
	}
	/**
	 * Базовая реализация ничего не делает
	 */
	public void updateEntity() throws Exception
	{
	}
	/**
	 * Базовая реализация ничего не делает
	 */
	public void updateView() throws Exception
	{
	}

}
