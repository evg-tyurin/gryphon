package gryphon.gui.impl;

import gryphon.Entity;
import gryphon.UserAction;
import gryphon.common.FriendlyException;
import gryphon.common.GryphonEvent;
import gryphon.common.ListView;

import java.util.Arrays;

public class DeleteFromListViewAction extends UserAction
{
    private ListView listView;

    public DeleteFromListViewAction()
    {
        super("Удалить");
    }

    public void doAction(GryphonEvent event) throws Exception
    {
        Entity[] selection = getListView().getSelection();
        if (selection.length == 0)
        {
            throw new FriendlyException("Ничего не выбрано");
        }
        getListView().getList().removeAll(Arrays.asList(selection));
    }

    public ListView getListView()
    {
        return listView;
    }

    public void setListView(ListView listView)
    {
        this.listView = listView;
    }

    public UserAction cloneMe() throws Exception
    {
        return (UserAction) clone();
    }
}