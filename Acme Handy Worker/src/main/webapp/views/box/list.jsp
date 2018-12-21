<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- 
	Recieves: 
		List<Box> boxes - los boxes de un actor.		
-->

<security:authorize access="isAuthenticated()">

	<display:table name="boxes" id="row" requestURI="box/list.do" pagesize="5">


		<spring:message code="box.boxes" var="boxNameHeader"/>
		<display:column property="name" title="${boxNameHeader}"/>

		<display:column>
			<a href="box/display.do?boxId=${row.id}"><spring:message code ='box.listMessages'/></a>
		</display:column>
		<jstl:if test="${row.systemBox == false}">
			<display:column>
				<a href="box/delete.do?boxId=${row.id}"><spring:message code ='box.delete'/></a>
			</display:column>
			<display:column>
				<a href="box/edit.do?boxId=${row.id}"><spring:message code ='box.edit'/></a>
			</display:column>
		</jstl:if>

	</display:table>
	
	<a href="box/create.do"><spring:message code='box.create'/></a>
</security:authorize>