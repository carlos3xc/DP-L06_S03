<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="${actors}" id="row" pagesize="5"
	requestURI="actor/admin/list.do">

	<display:column titleKey="actor.name" property="name" />

	<display:column titleKey="actor.middleName" property="middle" />

	<display:column titleKey="actor.surname" property="surname" />

	<display:column titleKey="actor.isBanned" property="banned" />

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="actor/admin/edit.do?actorId=${row.id}"> <spring:message
					code="admin.edit" />
			</a>
		</display:column>

	</security:authorize>

</display:table>
