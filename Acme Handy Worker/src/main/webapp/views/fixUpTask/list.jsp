<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
	
	<!-- PARAMETERS FROM CONTROLLER: fixUpTasks: Collection<FixUpTask>, tasks a mostrar
									 customer: Customer, customer logeado para comprobar si la task le pertenece -->
									 	
		<security:authorize access="hasRole('CUSTOMER')">
	
			<display:table name="fixUpTasks" id="row" requestURI="fixUpTask/customer/list.do" pagesize="5">
				
				<display:column titleKey="task.options">
					<a href="fixUpTask/show.do?fixUpTaskId=${row.id}">
								<spring:message	code="task.show" />
					</a><br/>
					<jstl:if test="${row.customer == customer}"> <!-- customer SE PASAR� DESDE EL CONTROLADOR INDICADO EL CUSTOMER LOGEADO -->
							<a href="fixUpTask/customer/edit.do?fixUpTaskId=${row.id}">
								<spring:message	code="task.edit" />
							</a><br/>							
					</jstl:if>
					<a href="complaint/customer/show.do?fixUpTaskId=${row.id}">
						<spring:message	code="task.complain" />
					</a><br/>
				</display:column>
				
				<display:column property="ticker" titleKey="task.ticker" />	
			
				<display:column property="description" titleKey="task.description" />
				
				<spring:message code="task.moment.format" var="formatMoment"/>
				<display:column property="moment" titleKey="task.moment" format="{0,date,${formatMoment} }"/>
				<display:column property="startMoment" titleKey="task.startMoment" format="{0,date,${formatMoment} }"/>
				
				<display:column property="maxPrice" titleKey="task.maxPrice" />	
			
			</display:table>
			
		<a href="fixUpTask/customer/create.do"><spring:message code="task.create"/></a>
		
		</security:authorize>
		
		<security:authorize access="hasRole('HANDYWORKER')">
	
			<display:table name="fixUpTasks" id="row" requestURI="fixUpTask/list.do" pagesize="5">
				
				<display:column titleKey="task.options">
					<a href="fixUpTask/show.do?fixUpTaskId=${row.id}">
						<spring:message	code="task.show" />
					</a><br/>
					
					<a href="application/handyWorker/apply.do?fixUpTaskId=${row.id}">
						<spring:message code="task.apply"/>
					</a><br/>	
					
					<a href="actor/profile.do?actorId=${row.customer.id}">
						<spring:message code="task.publisher"/>
					</a><br/>
								
				</display:column>
				
				<display:column property="ticker" titleKey="task.ticker" />	
			
				<display:column property="description" titleKey="task.description" />
				
				<spring:message code="task.moment.format" var="formatMoment"/>
				<display:column property="moment" titleKey="task.moment" format="{0,date,${formatMoment} }"/>			
				<display:column property="startMoment" titleKey="task.startMoment" format="{0,date,${formatMoment} }"/>
				
				<display:column property="maxPrice" title="task.maxPrice" />	
			
			</display:table>
					
		</security:authorize>