<%@ page contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>


<div id="submenu_ExpenseAccountantApproverSearchForm_div">
	<div class="submenu_bg">
		<s:if test="#session.EXPENSESACCOUNTANT_ADD == true">
			<sj:a href="setUpInsertOrUpdateExpAccountantApprover.action" targets="submenu_ExpenseAccountantApproverSearchForm_div" indicator="indicatorSubMenuExpenseAccountantApproverSearchFormId_div" cssClass="link"><s:text name="MTIAddExpenseAccountantApprover" /></sj:a> |
		</s:if>
		<s:if test="#session.EXPENSESACCOUNTANT_VIEW == true">
			<sj:a href="getAllExpAccountantApprover.action" targets="submenu_ExpenseAccountantApproverSearchForm_div" indicator="indicatorSubMenuExpenseAccountantApproverSearchFormId_div" cssClass="link"><s:text name="MTIViewExpenseAccountantApprover"/></sj:a> |
			<sj:a href="accountantSearchForm.action" targets="submenu_ExpenseAccountantApproverSearchForm_div" indicator="indicatorSubMenuExpenseAccountantApproverSearchFormId_div" cssClass="link"><s:text name="MTISearchExpenseAccountantApprover"/></sj:a>
		</s:if>
	</div>
		<br/>
		<img id="indicatorSubMenuExpenseAccountantApproverSearchFormId_div" src="${pageContext.request.contextPath}/resources/images/indicator.gif" alt="Loading..." style="display:none"/>
		
	<s:if test="#session.USER_NAME==null"><%try{response.sendRedirect("doLogout.action");}catch(Exception e){e.printStackTrace();}%></s:if>
	<jsp:include page="common/messages.jsp" flush="true"/>
	<s:form action="accountantSearchResult">
	<table class="maintable">
	      <tr>
	        <td align="center" ><table class="formouter">
	          <tr>
	            <td><table class="employeeformiinertable">
	                <tr>
	                  <td class="formtitle">
							<s:text name="label.title.expaccountapprover.search" />
					  </td>
	                </tr>
	                <tr>
	                  <td class="forminner"><table class="tablealign">
			<tr>
				<td class="inputtext"><s:text name="label.header.expaccountantapprover.accountantEmployee" /></td>
				<td class="employeeinputtd"><s:textfield name="expAccountantApprover.empFirstName" cssClass="employeeinput" /></td>
				<td class="inputerrormessage"></td>
			</tr>
		</table></td></tr></table></td></tr></table></td></tr></table>
		<br />
		<table align="center">
			<tr>
				<td>
					<img id="indicatorExpenseAccountantApprFormSearchId_div" src="${pageContext.request.contextPath}/resources/images/indicator.gif" alt="Loading..." style="display:none"/>				    
    		    	<sj:submit key="button.label.submit" cssClass="submitbutton117" targets="submenu_ExpenseAccountantApproverSearchForm_div" indicator="indicatorExpenseAccountantApprFormSearchId_div"/>
				</td>
				<td><s:reset key="button.label.reset"
					cssClass="resetbutton117"/></td>
			</tr>
		</table>
		<s:hidden name="expAccountantApprover.hcmoExpensesAccountantId" />
	</s:form>
</div>	