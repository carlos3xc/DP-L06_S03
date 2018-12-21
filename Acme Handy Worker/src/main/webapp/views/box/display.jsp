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

<h2><jstl:out value="${box.name}"/></h2>

	<display:table name="box.messages" id="row" requestURI="message/list.do" pagesize="5">

		<spring:message code="message.moment" var="momentMessageHeader"/>
		<display:column property="moment" title="${momentMessageHeader}"/>

		<spring:message code="message.subject" var="subjectMessageHeader"/>
		<display:column property="subject" title="${subjectMessageHeader}"/>

		<spring:message code="message.body" var="bodyMessageHeader"/>
		<display:column property="body" title="${bodyMessageHeader}"/>

		<spring:message code="message.priority" var="priorityMessageHeader"/>
		<display:column property="priority" title="${priorityMessageHeader}"/>

		<spring:message code="message.sender" var="senderMessageHeader"/>
		<display:column property="sender" title="${senderMessageHeader}"/>

		<display:column>
			<jstl:forEach items="${tags}" var="tag">
				<jstl:out value="${tag}; "/>
			</jstl:forEach>
		</display:column>

		<display:column>
		<jstl:forEach items="${recipients}" var="receiver">
			<jstl:out value="${receiver.userAccount.username}"/>
		</jstl:forEach>
		</display:column>

		<display:column>
			<jstl:forEach items="${recipients}" var="receiver">
				<form:form action="message/manage.do" modelAttribute="newBoxId">
					<form:select path="moveBox">
						<form:options items="${actors}" itemLabel="userAccount.username" itemValue="id"/>
					</form:select>
				</form:form>
			</jstl:forEach>
		</display:column>


	</display:table>
