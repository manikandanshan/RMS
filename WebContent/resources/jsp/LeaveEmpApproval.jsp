<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<div id="submenu_LeaveApprove_Emp_div">
<s:form action="getAllApprovedList">
	<table class="maintable">
      <tr>
        <td align="center" ><table class="formouter">
          <tr>
            <td><table class="employeeformiinertable">
                <tr>
                  <td class="formtitle">
						<s:text name="label.title.lrapp.Approved" />
                  </td>
                </tr>
                <tr>
                  <td class="forminner"><table class="tablealign">
				<tr>
        		   <td class="inputtext"><s:text name="label.header.common.empName"/></td>
         		   <td class="employeeinputtd">
         		   		<s:select 
         		   			name="lrapp.empIdObj.employeeId"
          					headerKey=""
            				headerValue="-- Please Select --"
           					list="#request.empsList" 
               				listKey="employeeId" 
              				listValue="empFullName"
              				cssClass="employeeselect"
     					  />
          		  </td>
          		  <td class="inputerrormessage"></td>
       		 </tr>
		</table></td></tr></table></td></tr></table></td></tr></table>
	<table align="center">
		<tr>
		   	<td class="nowrap">
			   	<img id="indicatorApprovedListImgForm" src="${pageContext.request.contextPath}/resources/images/indicator.gif" alt="Loading..." style="display:none"/>
		     	<sj:submit targets="submenu_LeaveApprove_Emp_div" cssClass="submitbutton117" indicator="indicatorApprovedListImgForm"></sj:submit>
		    </td>
        </tr>  		
	</table><br/><br><br>
</s:form><br><br>
</div>