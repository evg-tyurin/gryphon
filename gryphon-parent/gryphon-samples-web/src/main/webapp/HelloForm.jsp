<%@ page contentType="text/html; charset=windows-1251" %>
<%@ page import="gryphon.samples.web.*" %>
<%@ taglib uri="/gryphon-tags" prefix="gryphon" %>
<gryphon:form
title="Gryphon sample application"
north="gryphon.samples.web.HelloListView" northEntity="<%=HelloNames.HELLO%>"
>
<%-- 
<gryphon:button name="mybutton_1" value="�������������� ��������" tooltip="��� �������������� ��������� ��������" action="gryphon?action=404.jsp"/>
--%>

<gryphon:button action="<%=SampleActions.HELLO%>"/>
<p>
����������� �� ������ ������ [stamp=<%=Math.round(Math.random())%>]
</p>
</gryphon:form>
