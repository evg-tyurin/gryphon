package gryphon.samples.web;

import gryphon.common.DefaultDescriptor;
import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;
import gryphon.web.HtmlTableView;

public class HelloListView extends HtmlTableView {
  public HelloListView() {
  }
  public void init() throws java.lang.Exception {
    Descriptor d = new DefaultDescriptor();
    d.addEntry(new DescriptorEntry("язык","Language"));
    d.addEntry(new DescriptorEntry("ѕриветствие","Hello"));
    setDescriptor(d);
  }

}