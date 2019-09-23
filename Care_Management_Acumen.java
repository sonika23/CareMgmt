package dci.features.TestCases;


import java.lang.reflect.Method;
import java.sql.Driver;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;

import dci.features.CommonClassesReusable.AGlobalComponents;
import dci.features.CommonClassesReusable.BrowserSelection;
import dci.features.CommonClassesReusable.ReadDataFromPropertiesFile;
import dci.features.CommonClassesReusable.Utility;
import dci.features.CommonClassesReusable.dynamicwait;
import dci.features.CommonFunctions.LoginPage;
import dci.features.CommonFunctions.DCI_Care_Management_CommonMethod;
import dci.features.ObjectRepository.HomeObjects;


public class Care_Management_Acumen extends BrowserSelection {

	String testName;
	String S3BucketConfigFilePath =System.getProperty("user.dir")+"\\Browser_Files\\ConfigCM.txt"; 
	
	@BeforeClass
	public void Login() throws Exception {
		LoginPage.login(AGlobalComponents.username,AGlobalComponents.password);
		AGlobalComponents.password = "Milton@1234";
	}
	
	@BeforeMethod
	public void commonPage(Method method) throws Throwable{
		unhandledException = false;
		testName = method.getName();
		driver.navigate().refresh();
		dynamicwait.WAitUntilPageLoad();
		try{
			driver.findElement(By.xpath(HomeObjects.dciLogoImg)).click();
		}catch(Throwable e){
			driver.navigate().refresh();
			dynamicwait.WAitUntilPageLoad(); 
			driver.findElement(By.xpath(HomeObjects.dciLogoImg)).click();
		}
	}
	
	public static void isConditionX(boolean condition) {
		if(condition==false){
			throw new SkipException("skipp");
		}
	}
	
	@Test(priority=1)
	public void Care_Management_TC001() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC001 (DCI- 771, 772, 773, 774, 775)", "Settings for Plan of Care");
		System.out.println("[INFO]--> Care_Management_TC001 (DCI- 771, 772, 773, 774, 775) - TestCase Execution Begins");
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create a Supervisor/Employer ***************//
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmtOtherUserLogin(uniqueemployeeName.replace(" ", ""), EditClientName, EditclientFirstName, EditclientLastName, false);
		Thread.sleep(1000);
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmtOtherUserLogin(uniqueemployeeName.replace(" ", ""), EditClientName, EditclientFirstName, EditclientLastName, true );
		DCI_Care_Management_CommonMethod.revokeRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
		
		/** Employee given Employer role **/		
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		DCI_Care_Management_CommonMethod.verifyClientCareManagementEmployerLogin(uniqueemployeeName.replace(" ", ""), EditClientName);		
	}
	
	@Test(priority=2)
	public void Care_Management_TC002() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC002 (DCI- 827, 831 )", "Add Plan of care Admin Permission check at Employee profile & Manage Permission Window");
		System.out.println("[INFO]--> Care_Management_TC002 (DCI- 827, 831) - TestCase Execution Begins");
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//	
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create an Employee ***************//
		String employeeFirstName = "Tim";
		String employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditEmpName = EditEmpFirstName+ " " +EditEmpLastName;
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(EditEmpName, EditEmpFirstName, EditEmpLastName, false);		// Disable PlanOfCare 
		DCI_Care_Management_CommonMethod.managePermissionEmployee(EditEmpName, "Plan Of Care", false);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(EditEmpName, "Plan Of Care", true);
		//TurnOFf

	}
	
	@Test(priority=3)
	public void Care_Management_TC003() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC003 (DCI-776, 777)", "Verify 'Action Button' and Plan of Care Page");
		System.out.println("[INFO]--> Care_Management_TC003 (DCI-776, 777) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//	
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/*********************************    Check for Superuser    ***********************************/		
		DCI_Care_Management_CommonMethod.verifyNewPlanOfCarePage(EditClientName);
			
		System.out.println("\n******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");/** EMPLOYEE 1 **/	
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		DCI_Care_Management_CommonMethod.verifyNewPlanOfCarePageForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);
		DCI_Care_Management_CommonMethod.revokeRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
		
		/*********************************    Check for Employer    ************************************/
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");		
		DCI_Care_Management_CommonMethod.verifyNewPlanOfCarePageForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);
		DCI_Care_Management_CommonMethod.revokeRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		/**********************************     Check for Employee    ***********************************/
		System.out.println("\n******* TestCase running for EMPLOYEE role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYEE role ****");
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);
		DCI_Care_Management_CommonMethod.verifyNewPlanOfCarePageForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);	
	}

	@Test(priority=4)
	public void Care_Management_TC004() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC004 (DCI-777, 769, 770)", "Verify Addition of Plan of Care , Search filters for Plan of care, Result Set");
		System.out.println("[INFO]--> Care_Management_TC004 (DCI-777, 769, 770) - TestCase Execution Begins");
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/********************************    Check for Superuser    ***********************************/
		String effectiveAndEndDate = DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);	
		String [] splitDate = effectiveAndEndDate.split("-");
		String effectiveDate = splitDate[0];
		String endDate = splitDate[1];	
		DCI_Care_Management_CommonMethod.verifySearchFiltersForPlanOfCare(EditClientName, effectiveDate, endDate, uniqueCostCenterName);
		
		/*********************************    Check for Supervisor   ***********************************/		
		System.out.println("\n******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCareVerifySearchFilterForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName);
		
		/********************************    Check for Employer    ******************************************/
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCareVerifySearchFilterForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName);
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("\n******* TestCase running for EMPLOYEE role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYEE role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);
		DCI_Care_Management_CommonMethod.createNewPlanOfCareVerifySearchFilterForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName);
		
	}
	
	@Test(priority=5)
	public void Care_Management_TC005() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC005 (DCI-781, DCI-778)", "Verify Plan of Care details and Saving of Plan of care into Database");
		System.out.println("[INFO]--> Care_Management_TC005 (DCI-781, DCI-778) - TestCase Execution Begins and Saving of Plan of care into Database");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		String role = "Superuser";
		
		/*********************************    Check for Superuser    *********************************/
		String effectiveAndEndDate = DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
		String [] splitDate = effectiveAndEndDate.split("-");
		String effectiveDate = splitDate[0];
		String endDate = splitDate[1];
		
		
		DCI_Care_Management_CommonMethod.verifyPlanOfCareDetails(EditClientName, effectiveDate, endDate, uniqueCostCenterName,role);
		
		/*********************************    Check for Supervisor    ********************************/		
		System.out.println("\n******* TestCase running for Supervisor role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
		
		role = "Supervisor";
		DCI_Care_Management_CommonMethod.verifyPlanOfCareDetailsForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName, role);
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		role = "Employer";
		DCI_Care_Management_CommonMethod.verifyPlanOfCareDetailsForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName, role);
		
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("\n******* TestCase running for EMPLOYEE role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYEE role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);
		
		role = "Employee";
		DCI_Care_Management_CommonMethod.verifyPlanOfCareDetailsForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName, role);				
	}
	
	@Test(priority=6)
	public void Care_Management_TC006() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC006 (DCI-780)", "Verify Edit Plan of care");
		System.out.println("[INFO]--> Care_Management_TC006 (DCI-780) - TestCase Execution Begins");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		String effectiveAndEndDate = DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
		String [] splitDate = effectiveAndEndDate.split("-");
		String effectiveDate = splitDate[0];
		String endDate = splitDate[1];
			
		DCI_Care_Management_CommonMethod.verifyEditPlanOfCare(EditClientName, effectiveDate, endDate, uniqueCostCenterName);
	
		/*********************************    Check for Supervisor    ********************************/		
		System.out.println("\n******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
		
		
		DCI_Care_Management_CommonMethod.verifyEditPlanOfCareForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName);
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");		
		DCI_Care_Management_CommonMethod.verifyEditPlanOfCareForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName);
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("\n******* TestCase running for EMPLOYEE role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYEE role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);

		DCI_Care_Management_CommonMethod.verifyEditPlanOfCareForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName);
	
	}
	
	@Test(priority=7)
    public void Care_Management_TC007() throws Throwable 
    {
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
        logger =report.startTest("Care_Management_TC007 (DCI- 794 )", "Add Goal");
        System.out.println("[INFO]--> Care_Management_TC007 (DCI- 794) - TestCase Execution Begins");
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create Plan Of Care ************//
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
 		DCI_Care_Management_CommonMethod.verifyAddGoal(EditClientName);
		
 		//************ Add Goal and Duplicate Goal ************//
 		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No");
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "Yes");
		
		
		//\\****** CHECK - Supervisor ******//\\
		System.out.println("\n******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		//************ Create New Client and Edit ************//
		clientFirstName = "Arko";
		clientLastName = "Pet" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		EditclientFirstName = "Duke";
		EditclientLastName = "Sam" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 1 (SUPERVISOR) **/
		String employeeFirstName = "Mean";
		String employeeLastName = "A" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
		DCI_Care_Management_CommonMethod.verifyAndCreateGoalOtherUserLogin(employeeFirstName.concat(employeeLastName), EditClientName);

		//\\****** CHECK - Employer ******//\\
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		//************ Create New Client and Edit ************//
		clientFirstName = "Arko";
		clientLastName = "Pet" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		EditclientFirstName = "Duke";
		EditclientLastName = "Sam" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		/** EMPLOYEE 2 (EMPLOYER)**/
		employeeFirstName = "Mean";
		employeeLastName = "B" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("\n******* TestCase running for EMPLOYEE role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYEE role ****");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		DCI_Care_Management_CommonMethod.verifyAndCreateGoalOtherUserLogin(employeeFirstName.concat(employeeLastName), EditClientName);


		//\\****** CHECK - Employee ******//\\
		logger.log(LogStatus.INFO, "**** Check running for Role : Employee ****");
		//************ Create New Client and Edit ************//
		clientFirstName = "Arko";
		clientLastName = "Pet" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		EditclientFirstName = "Duke";
		EditclientLastName = "Sam" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		/** EMPLOYEE **/
		employeeFirstName = "Mean";
		employeeLastName = "C" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);	
		DCI_Care_Management_CommonMethod.verifyAndCreateGoalOtherUserLogin(employeeFirstName.concat(employeeLastName), EditClientName);	
	}
	
	@Test(priority=8)
	public void Care_Management_TC008() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC008 (DCI-790)", "Add Assessment");
		System.out.println("[INFO]--> Care_Management_TC008 (DCI-790) - Add Assessment");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		String effectiveAndEndDate = DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
		String [] splitDate = effectiveAndEndDate.split("-");
		String effectiveDate = splitDate[0];
		String endDate = splitDate[1];
			
		DCI_Care_Management_CommonMethod.verifyAddAssessment(EditClientName, effectiveDate, endDate, uniqueCostCenterName);

		/*********************************    Check for Supervisor    ********************************/		
		System.out.println("\n******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
		
		
		DCI_Care_Management_CommonMethod.verifyAddAssessmentForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName);
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");	
		DCI_Care_Management_CommonMethod.verifyAddAssessmentForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName);
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("\n******* TestCase running for EMPLOYEE role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYEE role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);
		DCI_Care_Management_CommonMethod.verifyAddAssessmentForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, uniqueCostCenterName);
	}
	
	@Test(priority=9)
	public void Care_Management_TC009() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC009 (DCI-786)", "Attachments tab on Plan of care details page");
		System.out.println("[INFO]--> Care_Management_TC009 (DCI-786) - Attachments tab on Plan of care details page");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		String effectiveAndEndDate = DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
		String [] splitDate = effectiveAndEndDate.split("-");
		String effectiveDate = splitDate[0];
		String endDate = splitDate[1];
			
		String attachmentName = DCI_Care_Management_CommonMethod.addNewAttachmentsPoc(EditClientName, effectiveDate, endDate);
		String roleType = "Superuser";
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabPoc(EditClientName, effectiveDate, endDate, attachmentName,roleType);
		
		/*********************************    Check for Supervisor    ********************************/		
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
				
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
				
		roleType = "Supervisor";
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabForOtherUserPoc(uniqueemployeeName.replace(" ", ""), EditClientName,roleType);
		
		/*********************************    Check for Employer      ************************************/
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		roleType = "Employer";
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabForOtherUserPoc(uniqueemployeeName.replace(" ", ""), EditClientName,roleType);
				
		/**********************************     Check for Employee    ****************************************/
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);
		roleType = "Employee";
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabForOtherUserPoc(uniqueemployeeName.replace(" ", ""), EditClientName,roleType);
		
	}
	
	@Test(priority=10)
	public void Care_Management_TC010() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC010 (DCI-785)", "Notes Tab on Plan of care details page");
		System.out.println("[INFO]--> Care_Management_TC010 (DCI-785) - Notes Tab on Plan of care details page");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		String effectiveAndEndDate = DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
		String [] splitDate = effectiveAndEndDate.split("-");
		String effectiveDate = splitDate[0];
		String endDate = splitDate[1];
			
		DCI_Care_Management_CommonMethod.addNewNotesPoc(EditClientName, effectiveDate, endDate);
		
		DCI_Care_Management_CommonMethod.verifyNotesTabDetailPagePoc(EditClientName, effectiveDate, endDate);
		
		/*********************************    Check for Supervisor    ********************************/		
		System.out.println("\n******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
			
		DCI_Care_Management_CommonMethod.verifyNotesTabDetailPageForOtherUserPoc(uniqueemployeeName.replace(" ", ""), EditClientName);
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");	
		DCI_Care_Management_CommonMethod.verifyNotesTabDetailPageForOtherUserPoc(uniqueemployeeName.replace(" ", ""), EditClientName);
	
		/**********************************     Check for Employee    ****************************************/
		System.out.println("\n******* TestCase running for EMPLOYEE role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYEE role ****");
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);

		DCI_Care_Management_CommonMethod.verifyNotesTabDetailPageForOtherUserPoc(uniqueemployeeName.replace(" ", ""), EditClientName);
		
	}
	
	@Test(priority=11)
	public void Care_Management_TC011() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC011 (DCI-787)", "Events tab on Plan of care details page");
		System.out.println("[INFO]--> Care_Management_TC011 (DCI-787) - Events tab on Plan of care details page");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		String effectiveAndEndDate = DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		String [] splitDate = effectiveAndEndDate.split("-");
		String effectiveDate = splitDate[0];
		String endDate = splitDate[1];	
		DCI_Care_Management_CommonMethod.verifyEventTabAndDetailsPoc(EditClientName, effectiveDate, endDate);

	}
		
	@Test(priority=12)
	public void Care_Management_TC012() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC012 (DCI-835)", "Add task Validations");
		System.out.println("[INFO]--> Care_Management_TC012 (DCI-835) - Add task Validations");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No");
		DCI_Care_Management_CommonMethod.addTaskValidation(EditClientName, goalName);

		/*********************************    Check for Supervisor    ********************************/		
		System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
		
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
				
		DCI_Care_Management_CommonMethod.addTaskValidationForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		DCI_Care_Management_CommonMethod.addTaskValidationForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);		
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);

		DCI_Care_Management_CommonMethod.addTaskValidationForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);
	}
	
	@Test(priority=13)
	public void Care_Management_TC013() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC013 (DCI-841, DCI-932)", "Adding of questions in task, Addition of canned questions");
		System.out.println("[INFO]--> Care_Management_TC013 (DCI-841, DCI-932) - Adding of questions in task, Addition of canned questions");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No");
		String questionNameAndAns = DCI_Care_Management_CommonMethod.addQuestionType();
		
		String [] quesAns = questionNameAndAns.split("-");
		String questionName = quesAns[0];
		String answerName = quesAns[1];
		
		DCI_Care_Management_CommonMethod.verifyAddingOfQuestionsInTaskForCanned(EditClientName, goalName, questionName, answerName);
		DCI_Care_Management_CommonMethod.verifyAddingOfQuestionsInTaskForCustom(EditClientName, goalName, questionName, answerName);

		/*********************************    Check for Supervisor    ********************************/		
		System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
		
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
				
		DCI_Care_Management_CommonMethod.verifyAddingOfQuestionsInTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		DCI_Care_Management_CommonMethod.verifyAddingOfQuestionsInTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);		
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);

		DCI_Care_Management_CommonMethod.verifyAddingOfQuestionsInTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);	
	}
	
	@Test(priority=14)
	public void Care_Management_TC014() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC014 (DCI-842)", "Verify Edit Task");
		System.out.println("[INFO]--> Care_Management_TC014 (DCI-842) - Verify Edit Task");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No");
		
		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	   
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes"); 
	    DCI_Care_Management_CommonMethod.editTask(combined);
	    
	    /********************************    Check for Supervisor    ********************************/	
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
				
		DCI_Care_Management_CommonMethod.verifyEditTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		DCI_Care_Management_CommonMethod.verifyEditTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);		
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
		clientFirstName = "Stevenn";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikee";
		EditclientLastName = "Glaimgs" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);

		DCI_Care_Management_CommonMethod.verifyEditTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);			
	}
		
	@Test(priority=15)
	public void Care_Management_TC015() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC015 (DCI- 797 )", "Goals tab on Plan of care details page");
		System.out.println("[INFO]--> Care_Management_TC015 (DCI- 797) - TestCase Execution Begins");
		
		/************ Create New Cost Center Code || Funding Source Code || Service Code ************/
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		/************ Create New Client and Edit ************/
		String clientFirstName = "Sam";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		/************ Edit Client ************/
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create Plan Of Care ************/
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
 		/************ Add Goal ************/
 		String goalName = "GOAL1-".concat( Utility.UniqueString(4));
 		String goalName1 = "GOAL2-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No");
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName1, false, "No");
		
		//************ Verify Goal Tab Details page (SUPER-USER) ************//
		DCI_Care_Management_CommonMethod.verifyGoalTabDetailsPage(EditClientName, goalName);
		
		/*********************************    Check for Supervisor    ********************************/		
		System.out.println("**** TestCase running for SUPERVISOR role ****");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		/************ Create New Client and Edit ************/
		clientFirstName = "Sam";
		clientLastName = "Rockas" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Timas" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "Kola" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a ** SUPERVISOR ** role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");

		//************ Verify Goal Tab Details page SUPERVISOR ************//
		DCI_Care_Management_CommonMethod.verifyGoalTabDetailsPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("**** TestCase running for EMPLOYER role ****");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		clientFirstName = "Sam";
		clientLastName = "Rockw" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Timt" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a ** EMPLOYER ** role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//************ Verify Goal Tab Details page EMPLOYER ************//
		DCI_Care_Management_CommonMethod.verifyGoalTabDetailsPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("Creating a ** EMPLOYER ** role from Employee");
		logger.log(LogStatus.INFO, "**** TestCase running for ** EMPLOYEE ** role ****");
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Cook" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create an Employee ***************/
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);
		
		//************ Verify Goal Tab Details page EMPLOYEE ************//
		DCI_Care_Management_CommonMethod.verifyGoalTabDetailsPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
	}

	@Test(priority=16)
	public void Care_Management_TC016() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC016 (DCI- 799, 800 )", "Saving of Goal information into database and Goal Details");
		System.out.println("[INFO]--> Care_Management_TC016 (DCI- 799, 800) - TestCase Execution Begins");
		
		/************ Create New Cost Center Code || Funding Source Code || Service Code ************/
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		/************ Create New Client and Edit ************/
		String clientFirstName = "Sam";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		/************ Edit Client ************/
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create Plan Of Care ************/
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
 		/************ Add Goal ************/
 		String goalName = "GOAL1-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No");
		
		//************ Verify Goal Details page (SUPER-USER) ************//
		DCI_Care_Management_CommonMethod.verifyGoalDetailsPage(EditClientName, goalName, "SUPERUSER");
		
		/*********************************    Check for Supervisor    ********************************/	
		
		System.out.println("******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		
		/************ Create New Client and Edit ************/
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");			
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a ** SUPERVISOR ** role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");

		//************ Verify Goal Details page SUPERVISOR ************//
		DCI_Care_Management_CommonMethod.verifyGoalDetailsPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/*********************************    Check for Employer      ************************************/
		
		System.out.println("******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a ******* EMPLOYER ******* role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//************ Verify Goal Details page EMPLOYER ************//
		DCI_Care_Management_CommonMethod.verifyGoalDetailsPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/**********************************     Check for Employee    ****************************************/
		
		System.out.println( "**** TestCase running for ** EMPLOYEE ** role ****");
		logger.log(LogStatus.INFO, "**** TestCase running for ** EMPLOYEE ** role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Cook" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create an Employee ***************/
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);
		
		//************ Verify Goal Details page EMPLOYEE ************//
		DCI_Care_Management_CommonMethod.verifyGoalDetailsPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
	}
	
	@Test(priority=17)
	public void Care_Management_TC017() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC017 (DCI- 801 )", "Renew Goal");
		System.out.println("[INFO]--> Care_Management_TC017 (DCI- 801) - TestCase Execution Begins");
		
		/************ Create New Cost Center Code || Funding Source Code || Service Code ************/
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		/************ Create New Client and Edit ************/
		String clientFirstName = "Sam";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		/************ Edit Client ************/
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create Plan Of Care ************/
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
 		/************ Add Goal ************/
 		String goalName = "GOAL1-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No");
		
		//************ Verify Renew Goal Functionality (SUPER-USER) ************//
		DCI_Care_Management_CommonMethod.verifyReNewGoal(EditClientName, goalName);
		
		/*********************************    Check for Supervisor    ********************************/	
		
		System.out.println("******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		
		/************ Create New Client and Edit ************/
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");			
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a ** SUPERVISOR ** role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");

		//************ Verify renew Goal SUPERVISOR ************//
		DCI_Care_Management_CommonMethod.verifyReNewGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/*********************************    Check for Employer      ************************************/
		
		System.out.println("******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a ******* EMPLOYER ******* role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//************ Verify renew Goal EMPLOYER ************//
		DCI_Care_Management_CommonMethod.verifyReNewGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/**********************************     Check for Employee    ****************************************/
		
		System.out.println( "**** TestCase running for ** EMPLOYEE ** role ****");
		logger.log(LogStatus.INFO, "**** TestCase running for ** EMPLOYEE ** role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Cook" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create an Employee ***************/
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);
		
		//************ Verify renew Goal EMPLOYEE ************//
		DCI_Care_Management_CommonMethod.verifyReNewGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);	
	}
	
	@Test(priority=18)
	public void Care_Management_TC018() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC018 (DCI- 802 )", "Edit Goal");
		System.out.println("[INFO]--> Care_Management_TC018 (DCI- 802) - TestCase Execution Begins");
		
		/************ Create New Cost Center Code || Funding Source Code || Service Code ************/
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		/************ Create New Client and Edit ************/
		String clientFirstName = "Sam";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		/************ Edit Client ************/
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create Plan Of Care ************/
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
 		/************ Add Goal ************/
 		String goal1Name = "GOAL1-".concat( Utility.UniqueString(4));
 		String goal2Name = "GOAL2-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goal1Name, true, "No");
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goal2Name, true, "No");
		
		//************ Verify Edit Goal Functionality (SUPER-USER) ************//
		DCI_Care_Management_CommonMethod.verifyEditGoal(EditClientName, goal1Name, goal2Name);
		
		/*********************************    Check for Supervisor    ********************************/	
		
		System.out.println("******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		
		/************ Create New Client and Edit ************/
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");			
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a ** SUPERVISOR ** role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");

		//************ Verify Edit Goal Details page SUPERVISOR ************//
		DCI_Care_Management_CommonMethod.verifyEditGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/*********************************    Check for Employer      ************************************/
		
		System.out.println("******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a ******* EMPLOYER ******* role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//************ Verify Edit Goal Details page EMPLOYER ************//
		DCI_Care_Management_CommonMethod.verifyEditGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/**********************************     Check for Employee    ****************************************/
		
		System.out.println( "**** TestCase running for ** EMPLOYEE ** role ****");
		logger.log(LogStatus.INFO, "**** TestCase running for ** EMPLOYEE ** role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Cook" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create an Employee ***************/
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);
		
		//************ Verify Edit Goal Details page EMPLOYEE ************//
		DCI_Care_Management_CommonMethod.verifyEditGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);	
	}

	
	@Test(priority=19)  
	public void Care_Management_TC019() throws Throwable 
	{
//		isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC019 (DCI- 822 )", "Task Tab on Goal Details page");
		System.out.println("[INFO]--> Care_Management_TC019 (DCI- 822) - TestCase Execution Begins");
		
		/************ Create New Cost Center Code || Funding Source Code || Service Code ************/
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundngSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Servic_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		/************ Create New Client and Edit ************/
		String clientFirstName = "Sam";
		String clientLastName = "Roch" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		/************ Edit Client ************/
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Tin" + Utility.UniqueNumber(3).toLowerCase();	
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create Plan Of Care ************/
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
 		/************ Add Goal ************/
	
 		String goal1Name = "GOAL1-".concat( Utility.UniqueString(4));
 		String goal2Name = "GOAL2-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goal1Name, true, "No");
		
		String uniqueTaskName = "Task-"+ Utility.UniqueString(3);
		String taskName = DCI_Care_Management_CommonMethod.createTask(EditClientName+" "+goal1Name+" "+uniqueTaskName, true, "No");		//Active
		DCI_Care_Management_CommonMethod.createTask(EditClientName+" "+goal1Name+" "+uniqueTaskName+"1", false, "No");				//Inactive
		
		//************ Verify Task Tab Goal Functionality (SUPER-USER) ************//
		
		/*String EditClientName = "Mike Tin890", goal1Name ="GOAL1-HNja", taskName="Task-PTG";*/
		
		DCI_Care_Management_CommonMethod.verifyTaskTabGoalDetailsPage(EditClientName, goal1Name, taskName);
		
		/*********************************    Check for Supervisor    ********************************/	
		System.out.println("******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		
		/************ Create New Client and Edit ************/
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");			
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Meal";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a ** SUPERVISOR ** role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");

		//************ Verify TaskTab Goal Details page SUPERVISOR ************//
		DCI_Care_Management_CommonMethod.verifyTaskTabGoalDetailsPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/*********************************    Check for Employer      ************************************/
		
		System.out.println("******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Means";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a ******* EMPLOYER ******* role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//************ Verify Task Tab Goal Details page EMPLOYER ************//
		DCI_Care_Management_CommonMethod.verifyTaskTabGoalDetailsPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);
		
		/**********************************     Check for Employee    ****************************************/
		
		System.out.println( "**** TestCase running for ** EMPLOYEE ** role ****");
		logger.log(LogStatus.INFO, "**** TestCase running for ** EMPLOYEE ** role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Cook" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create an Employee ***************/
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);
		
		//************ Verify Task tab Goal Details page EMPLOYEE ************//
		DCI_Care_Management_CommonMethod.verifyTaskTabGoalDetailsPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName);	
	}

	@Test(priority=20)
	public void Care_Management_TC020() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC020 (DCI- 824 )", "Notes Tab on Goal detail page");
		System.out.println("[INFO]--> Care_Management_TC020 (DCI- 824) - TestCase Execution Begins");
		
		/************ Create New Cost Center Code || Funding Source Code || Service Code ************/
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		/************ Create New Client and Edit ************/
		String clientFirstName = "Sam";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		/************ Edit Client ************/
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();	
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create Plan Of Care & Add Goal  ************/
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
 		String goal1Name = "GOAL1-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goal1Name, true, "No");
		
		/************ Add Note << Goal Functionality (SUPER-USER) ************/
		DCI_Care_Management_CommonMethod.verifyNewNotesGoal(EditClientName, goal1Name);
		DCI_Care_Management_CommonMethod.addNewNotesGoal(EditClientName, goal1Name);
		DCI_Care_Management_CommonMethod.verifyGoalNotesTabDetailPage(EditClientName, goal1Name, "SuperUser");
		
		/*********************************    Check for Supervisor    ********************************/	
		System.out.println("\n******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		
		/************ Create New Client and Edit ************/
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");			
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a ** SUPERVISOR ** role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");

		//************ Verify added Notes Goal Details page SUPERVISOR ************//
		DCI_Care_Management_CommonMethod.verifyGoalNotesTabDetailPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName, "Supervisor");
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a ******* EMPLOYER ******* role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//************ Verify added Notes Goal Details page EMPLOYER ************//
		DCI_Care_Management_CommonMethod.verifyGoalNotesTabDetailPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName, "Employer");
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println( "\n**** TestCase running for ** EMPLOYEE ** role ****");
		logger.log(LogStatus.INFO, "**** TestCase running for ** EMPLOYEE ** role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Cook" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create an Employee ***************/
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		
		//************ Verify added Notes Goal Details page EMPLOYEE ************//
		DCI_Care_Management_CommonMethod.verifyGoalNotesTabDetailPageForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName, "Employee");	
	}

	@Test(priority=21)
	public void Care_Management_TC021() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC021 (DCI- 825 )", "Attachments tab on Goal details page");
		System.out.println("[INFO]--> Care_Management_TC021 (DCI- 825) - TestCase Execution Begins");

		/************ Create New Cost Center Code || Funding Source Code || Service Code ************/
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrcew_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		/************ Create New Client and Edit ************/
		String clientFirstName = "Sam";
		String clientLastName = "Rocks" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		/************ Edit Client ************/
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Tims" + Utility.UniqueNumber(3).toLowerCase();	
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create Plan Of Care & Add Goal  ************/
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
 		String goal1Name = "GOAL1-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goal1Name, true, "No");
		
		/************ Add Attachments << Goal Functionality (SUPER-USER) ************/
		String attachmentName = DCI_Care_Management_CommonMethod.addNewAttachmentsGoal(EditClientName, goal1Name);
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabGoal(EditClientName, goal1Name, attachmentName, "SuperUser");
			
		/*********************************    Check for Supervisor    ********************************/	
		System.out.println("\n******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		
		/************ Create New Client and Edit ************/
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");			
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a ** SUPERVISOR ** role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");

		//************  Verify Goal << Attachment  Details page SUPERVISOR ************//
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName, "Supervisor");
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rocks" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tims" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a ******* EMPLOYER ******* role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//************ Verify Goal << Attachment Details page EMPLOYER ************//
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName, "Employer");
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println( "\n**** TestCase running for ** EMPLOYEE ** role ****");
		logger.log(LogStatus.INFO, "**** TestCase running for ** EMPLOYEE ** role ****");
		
		clientFirstName = "Duke";
		clientLastName = "Rocks" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Saks" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create an Employee ***************/
		employeeFirstName = "Tim";
		employeeLastName = "P" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);
		
		//************ Verify Goal << Attachment Details page EMPLOYEE ************//
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName, "Employee");	
	}
	
	@Test(priority=22)
	public void Care_Management_TC022() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC022 (DCI- 826 )", "Events tab on Goal details page");
		System.out.println("[INFO]--> Care_Management_TC022 (DCI- 826) - TestCase Execution Begins");

		/************ Create New Cost Center Code || Funding Source Code || Service Code ************/
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		/************ Create New Client and Edit ************/
		String clientFirstName = "Sam";
		String clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		/************ Edit Client ************/
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();	
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create Plan Of Care & Add Goal  ************/
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
 		String goal1Name = "GOAL1-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goal1Name, true, "No");
		
		/************ Add Attachments << Goal Functionality (SUPER-USER) ************/
		String attachmentName = DCI_Care_Management_CommonMethod.addNewAttachmentsGoal(EditClientName, goal1Name);
		DCI_Care_Management_CommonMethod.addNewNotesGoal(EditClientName, goal1Name);
		DCI_Care_Management_CommonMethod.verifyEventTabGoal(EditClientName, goal1Name);
		
		/*********************************    Check for Supervisor    ********************************/	
		System.out.println("\n******* TestCase running for SUPERVISOR role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for SUPERVISOR role ****");
		
		/************ Create New Client and Edit ************/
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");			
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a ** SUPERVISOR ** role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");

		//************  Verify Goal << Attachment  Details page SUPERVISOR ************//
		DCI_Care_Management_CommonMethod.verifyEventTabGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName, "Supervisor");
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("\n******* TestCase running for EMPLOYER role *******");
		logger.log(LogStatus.INFO, "**** TestCase running for EMPLOYER role ****");
		
		clientFirstName = "Sam";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Tim" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a ******* EMPLOYER ******* role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//************ Verify Goal << Attachment Details page EMPLOYER ************//
		DCI_Care_Management_CommonMethod.verifyEventTabGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName, "Employer");
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println( "\n**** TestCase running for ** EMPLOYEE ** role ****");
		logger.log(LogStatus.INFO, "**** TestCase running for ** EMPLOYEE ** role ****");
		
		clientFirstName = "Duke";
		clientLastName = "Rock" + Utility.UniqueNumber(3).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		/************ Edit Client ************/
		EditclientFirstName = "Mike";
		EditclientLastName = "Sak" + Utility.UniqueNumber(3).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/************ Create an Employee ***************/
		employeeFirstName = "Tim";
		employeeLastName = "P" + Utility.UniqueString(3).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);
		
		//************ Verify Goal << Attachment Details page EMPLOYEE ************//
		DCI_Care_Management_CommonMethod.verifyEventTabGoalForOtherUser(uniqueemployeeName.replace(" ",""), EditClientName, uniqueCostCenterName, "Employee");
	}
	
	@Test(priority=23)
	public void Care_Management_TC023() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC023 (DCI-843, DCI-837)", "Verify Task Details and Saving of task information in database");
		System.out.println("[INFO]--> Care_Management_TC023 (DCI-843, DCI-837) - Verify Task Details and Saving of task information in database");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
		
		String role = "Superuser";
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
	    DCI_Care_Management_CommonMethod.verifyTaskDetailPage(EditClientName, goalName, role);
	  
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
        logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
        role = "Supervisor";
	    
 		//************ Create New Client and Edit ************//
 		clientFirstName = "Steven";
 		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
 		uniqueClientName = clientFirstName+ " " +clientLastName;
 		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
 				
 		//************ Edit Client ************//
 		EditclientFirstName = "Mike";
 		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		EditClientName = EditclientFirstName+ " " +EditclientLastName;
 		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
 			
 		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
 		
 		/** Employee given Supervisor role **/
 		System.out.println("Creating a Supervisor role from Employee");
 		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
 		
 		
 		DCI_Care_Management_CommonMethod.verifyTaskDetailPageForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName,role);
 		
 		/*********************************    Check for Employer      ************************************/
 		System.out.println("* TestCase running for EMPLOYER role *");
 	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
 		
 		role = "Employer";
 		
 		clientFirstName = "Steven";
 		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
 		uniqueClientName = clientFirstName+ " " +clientLastName;
 		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
 				
 		//************ Edit Client ************//
 		EditclientFirstName = "Mike";
 		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		EditClientName = EditclientFirstName+ " " +EditclientLastName;
 		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
 		
 		/** EMPLOYEE 2 **/
 		employeeFirstName = "Mean";
 		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
 		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

 		/** Employee given Employer role **/
 		System.out.println("Creating a Employer role from Employee");
 		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
 		
 		DCI_Care_Management_CommonMethod.verifyTaskDetailPageForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, role);		
 		
 		/**********************************     Check for Employee    ****************************************/
 		System.out.println("* TestCase running for EMPLOYEE role *");
 	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
 		
 		role = "Employee";
 		clientFirstName = "Steven";
 		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
 		uniqueClientName = clientFirstName+ " " +clientLastName;
 		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
 				
 		//************ Edit Client ************//
 		EditclientFirstName = "Mike";
 		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		EditClientName = EditclientFirstName+ " " +EditclientLastName;
 		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
 		
 		//************ Create an Employee ***************//
 		employeeFirstName = "Tim";
 		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
 		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
 				
 		//************ Edit an Employee ***************//
 		String EditEmpFirstName = "Mike";
 		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
 		Thread.sleep(1000);

 		DCI_Care_Management_CommonMethod.verifyTaskDetailPageForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, role);		
 	}
	
	@Test(priority=24)
	public void Care_Management_TC024() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC024 (DCI-913)", "Verify Notes Tab on Task detail page");
		System.out.println("[INFO]--> Care_Management_TC024 (DCI-913) - Verify Notes Tab on Task detail page");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
		
		String roleType = "Superuser";
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
		
		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");
	    DCI_Care_Management_CommonMethod.addNotesForTask(EditClientName,goalName,uniqueTaskName);		
	    
	    DCI_Care_Management_CommonMethod.verifyNotesTabDetailPageForTask(EditClientName,goalName,uniqueTaskName, roleType);
	    
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
        logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
        roleType = "Supervisor";
	    
 		//************ Create New Client and Edit ************//
 		clientFirstName = "Steven";
 		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
 		uniqueClientName = clientFirstName+ " " +clientLastName;
 		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
 				
 		//************ Edit Client ************//
 		EditclientFirstName = "Mike";
 		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		EditClientName = EditclientFirstName+ " " +EditclientLastName;
 		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
 			
 		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
 		
 		/** Employee given Supervisor role **/
 		System.out.println("Creating a Supervisor role from Employee");
 		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
 			
 		DCI_Care_Management_CommonMethod.verifyNotesTabDetailPageForTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName,roleType);
 		
 		/*********************************    Check for Employer      ************************************/
 		System.out.println("* TestCase running for EMPLOYER role *");
 	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
 		
 		roleType = "Employer";
 		
 		clientFirstName = "Steven";
 		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
 		uniqueClientName = clientFirstName+ " " +clientLastName;
 		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
 				
 		//************ Edit Client ************//
 		EditclientFirstName = "Mike";
 		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		EditClientName = EditclientFirstName+ " " +EditclientLastName;
 		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
 		
 		/** EMPLOYEE 2 **/
 		employeeFirstName = "Mean";
 		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
 		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

 		/** Employee given Employer role **/
 		System.out.println("Creating a Employer role from Employee");
 		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
 		
 		DCI_Care_Management_CommonMethod.verifyNotesTabDetailPageForTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, roleType);		
 		
 		/**********************************     Check for Employee    ****************************************/
 		System.out.println("* TestCase running for EMPLOYEE role *");
 	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
 		
 		roleType = "Employee";
 		clientFirstName = "Steven";
 		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
 		uniqueClientName = clientFirstName+ " " +clientLastName;
 		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
 				
 		//************ Edit Client ************//
 		EditclientFirstName = "Mike";
 		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		EditClientName = EditclientFirstName+ " " +EditclientLastName;
 		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
 		
 		//************ Create an Employee ***************//
 		employeeFirstName = "Tim";
 		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
 		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
 				
 		//************ Edit an Employee ***************//
 		String EditEmpFirstName = "Mike";
 		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
 		Thread.sleep(1000);

 		DCI_Care_Management_CommonMethod.verifyNotesTabDetailPageForTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, roleType);		
 	}
	   
	@Test(priority=25)
	public void Care_Management_TC025() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC025 (DCI-914)", "Verify Attachments tab on Task details page");
		System.out.println("[INFO]--> Care_Management_TC025 (DCI-914) - Verify Attachments tab on Task details page");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mike";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/*********************************    Check for Superuser    *********************************/
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
		
		String roleType = "Superuser";
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
		
		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		
	    String attachmentName = DCI_Care_Management_CommonMethod.addAttachmentsForTask(EditClientName,goalName,uniqueTaskName);		
	    DCI_Care_Management_CommonMethod.verifyAttachmentSearchGridForTask(EditClientName,goalName,uniqueTaskName, attachmentName, roleType);
	    
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
        logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
        roleType = "Supervisor";
	    
 		//************ Create New Client and Edit ************//
 		clientFirstName = "Steven";
 		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
 		uniqueClientName = clientFirstName+ " " +clientLastName;
 		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
 				
 		//************ Edit Client ************//
 		EditclientFirstName = "Mike";
 		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		EditClientName = EditclientFirstName+ " " +EditclientLastName;
 		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
 			
 		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
 		
 		/** Employee given Supervisor role **/
 		System.out.println("Creating a Supervisor role from Employee");
 		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
 				
 		DCI_Care_Management_CommonMethod.verifyAttachmentsTabDetailPageForTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName,roleType);
 		
 		/*********************************    Check for Employer      ************************************/
 		System.out.println("* TestCase running for EMPLOYER role *");
 	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
 		
 		roleType = "Employer";
 		
 		clientFirstName = "Steven";
 		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
 		uniqueClientName = clientFirstName+ " " +clientLastName;
 		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
 				
 		//************ Edit Client ************//
 		EditclientFirstName = "Mike";
 		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		EditClientName = EditclientFirstName+ " " +EditclientLastName;
 		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
 		
 		/** EMPLOYEE 2 **/
 		employeeFirstName = "Mean";
 		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
 		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

 		/** Employee given Employer role **/
 		System.out.println("Creating a Employer role from Employee");
 		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
 		
 		DCI_Care_Management_CommonMethod.verifyAttachmentsTabDetailPageForTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, roleType);		
 		
 		/**********************************     Check for Employee    ****************************************/
 		System.out.println("* TestCase running for EMPLOYEE role *");
 	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
 		
 		roleType = "Employee";
 		clientFirstName = "Steven";
 		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
 		uniqueClientName = clientFirstName+ " " +clientLastName;
 		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
 				
 		//************ Edit Client ************//
 		EditclientFirstName = "Mike";
 		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		EditClientName = EditclientFirstName+ " " +EditclientLastName;
 		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
 		
 		//************ Create an Employee ***************//
 		employeeFirstName = "Tim";
 		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
 		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
 				
 		//************ Edit an Employee ***************//
 		String EditEmpFirstName = "Mike";
 		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
 		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
 		Thread.sleep(1000);

 		DCI_Care_Management_CommonMethod.verifyAttachmentsTabDetailPageForTaskForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName, roleType);	    
	}
	
	@Test(priority=26)
	public void Care_Management_TC026() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger=report.startTest("Care_Management_TC026 (DCI-833)", "Verify Add task");
		System.out.println("[Info]-->Care_Management_TC026 (DCI-833)-Verify Add task");
		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
						
		/*********************************    Check for Superuser    *********************************/
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
		
		String uniqueGoalName="Goal-".concat(Utility.UniqueString(3).toLowerCase());
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, uniqueGoalName, true, "No");
		
        String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
        String combined=EditClientName +" " +uniqueGoalName+" " +uniqueTaskName;
        DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");
        DCI_Care_Management_CommonMethod.verifyTaskfields(EditClientName, uniqueGoalName, "No");
   
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
				
		DCI_Care_Management_CommonMethod.createTaskAndverifyTaskFieldForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
			
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		DCI_Care_Management_CommonMethod.createTaskAndverifyTaskFieldForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);		
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Tim";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
				
		//************ Edit an Employee ***************//
		String EditEmpFirstName = "Mike";
		String EditEmpLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueemployeeName, EditEmpFirstName, EditEmpLastName, true);		// Enable PlanOfCare 
		Thread.sleep(1000);

		DCI_Care_Management_CommonMethod.createTaskAndverifyTaskFieldForOtherUser(uniqueemployeeName.replace(" ", ""), EditClientName);	    
	}

	@Test(priority=27)
	public void Care_Management_TC027() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC027 (DCI-922,  DCI-943)", "Verify Add Remove Goals and Add task result");
		System.out.println("[INFO]--> Care_Management_TC027 (DCI-922,  DCI-943) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10";
		double burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		String payRate="10";
		double payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    String roleType = "SuperUser";
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();   
		String getMonth=punchIDOrGetMonth[1].toString();   
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);

		
		/********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyAddTaskResultForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
	
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyAddTaskResultForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
					
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyAddTaskResultForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
							
	}
		
	@Test(priority=28)
	public void Care_Management_TC028() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC028 (DCI-947)", "Verify Notes Tab on add care note detail page");
		System.out.println("[INFO]--> Care_Management_TC028 (DCI-947) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaimshn" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10";
		double burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		String payRate="10";
		double payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

				
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();   
		String getMonth=punchIDOrGetMonth[1].toString();   
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		
		DCI_Care_Management_CommonMethod.verifyAddNotesForAddCareNotesPage(EditClientName,punchId, roleType);
	
		DCI_Care_Management_CommonMethod.verifyNotesTabSearchFunctionalityForAddCareNotesPage(EditClientName, punchId, roleType);
	
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.addPunchTaskResultNotesFunctionalityForAddCareNotesForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
	
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.addPunchTaskResultNotesFunctionalityForAddCareNotesForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
					
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.addPunchTaskResultNotesFunctionalityForAddCareNotesForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
						
	}
		
	@Test(priority=29)
	public void Care_Management_TC029() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC029 (DCI-948)", "Verify Attachments tab on add care note detail page");
		System.out.println("[INFO]--> Care_Management_TC029 (DCI-948) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaimshn" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10";
		double burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		String payRate="10";
		double payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		
		
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();   
		String getMonth=punchIDOrGetMonth[1].toString();   
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		
		String attachmentName = DCI_Care_Management_CommonMethod.verifyAddAttachmentsForAddCareNotesPage(EditClientName,punchId, roleType);
	
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabSearchFunctionalityForAddCareNotesPage(EditClientName, punchId, roleType, attachmentName);
		
		
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.addPunchTaskResultAttachmentsFunctionalityForAddCareNotesForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
	
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.addPunchTaskResultAttachmentsFunctionalityForAddCareNotesForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
					
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.addPunchTaskResultAttachmentsFunctionalityForAddCareNotesForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
	}
	
	@Test(priority=30)
	public void Care_Management_TC030() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC030 (DCI-916)", "Verify Task Result Page");
		System.out.println("[INFO]--> Care_Management_TC030 (DCI-916) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10";
		double burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		String payRate="10";
		double payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    String roleType = "SuperUser";
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();   
		String getMonth=punchIDOrGetMonth[1].toString();   
		
		String[] careNoteAndTaskResultId = DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		String careNoteId = careNoteAndTaskResultId[0];
		String taskResultId = careNoteAndTaskResultId[1];
		String combineIdAndGoal = careNoteId+"-"+taskResultId+"-"+goalName+"-"+uniqueTaskName;
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResultDetailPage(EditClientName,punchId,roleType,combineIdAndGoal);
		
		/********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
			
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName+"-"+goalName;
		DCI_Care_Management_CommonMethod.verifyAddTaskResultDetailPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			

		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName+"-"+goalName;
		DCI_Care_Management_CommonMethod.verifyAddTaskResultDetailPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName+"-"+goalName;
		DCI_Care_Management_CommonMethod.verifyAddTaskResultDetailPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
							
	}
	
	@Test(priority=31)
	public void Care_Management_TC031() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC031 (DCI-923, DCI-924)", "Verify Edit Task result, Delete Task result");
		System.out.println("[INFO]--> Care_Management_TC031 (DCI-923, DCI-924) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10";
		double burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		String payRate="10";
		double payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    String roleType = "SuperUser";
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();   
		String getMonth=punchIDOrGetMonth[1].toString();   
		
		String[] careNoteAndTaskResultId = DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		String careNoteId = careNoteAndTaskResultId[0];
		String taskResultId = careNoteAndTaskResultId[1];
		String combineIdAndGoal = careNoteId+"-"+taskResultId+"-"+goalName+"-"+uniqueTaskName;
		
		DCI_Care_Management_CommonMethod.verifyEditAndDeleteTaskResult(EditClientName,punchId,roleType,combineIdAndGoal);
		
		/********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName+"-"+goalName;
		DCI_Care_Management_CommonMethod.verifyEditAndDeleteTaskResultForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
	
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName+"-"+goalName;
		DCI_Care_Management_CommonMethod.verifyEditAndDeleteTaskResultForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName+"-"+goalName;
		DCI_Care_Management_CommonMethod.verifyEditAndDeleteTaskResultForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
							
	}
		
	@Test(priority=32)
	public void Care_Management_TC032() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC032 (DCI-844, DCI-911, DCI-912)", "Verify 'Task Results tab' and 'Employees tab' on Task detail page, Data on Task result tab");
		System.out.println("[INFO]--> Care_Management_TC032 (DCI-844, DCI-911, DCI-912) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaimssh" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kkk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10";
		double burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		String payRate="10";
		double payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    String roleType = "SuperUser";
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();   
		String getMonth=punchIDOrGetMonth[1].toString();   
		
		String[] careNoteAndTaskResultId = DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		String careNoteId = careNoteAndTaskResultId[0];
		String taskResultId = careNoteAndTaskResultId[1];
		String combineIdAndGoal = careNoteId+"-"+taskResultId+"-"+goalName+"-"+uniqueTaskName+"-"+uniqueemployeeName;
		
		DCI_Care_Management_CommonMethod.verifyTaskResultAndByEmployeeOnTaskDetailPage(EditClientName,punchId,roleType,combineIdAndGoal);
		
		/********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName+"-"+goalName;
		DCI_Care_Management_CommonMethod.verifyTaskResultAndByEmployeeOnTaskDetailPageForOtherUser(uniqueemployeeName.replace(" ", ""),roleType,combineData);
		
		
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName+"-"+goalName;
		DCI_Care_Management_CommonMethod.verifyTaskResultAndByEmployeeOnTaskDetailPageForOtherUser(uniqueemployeeName.replace(" ", ""),roleType,combineData);
				
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		burdenMultiplier = Double.parseDouble(BurdenMultiplier); 
		payRate="10";
		payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName+"-"+goalName;
		DCI_Care_Management_CommonMethod.verifyTaskResultAndByEmployeeOnTaskDetailPageForOtherUser(uniqueemployeeName.replace(" ", ""),roleType,combineData);
		
	}
		
	@Test(priority=33)
	public void Care_management_TC033() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger=report.startTest("Care_management_TC033 (DCI-931, DCI-933,DCI-935, DCI-936, DCI-937", "Task Question Functionality");	
		System.out.println("[Info]-->Care_management_TC033 (DCI-931, DCI-933,DCI-935, DCI-936, DCI-937)-Task Question Functionality ");	
		
		/*********************************    Check for SuperUser  *********************************/
		System.out.println("* TestCase running for SUPERUSER Role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER Role ");	    
		String role="SuperUser";
		String permission="No Permission";
		String[] questionNameAndAnswer = DCI_Care_Management_CommonMethod.addQuestionTypeForAllUSers(role,permission);
		String questionName = questionNameAndAnswer[0];	 
		DCI_Care_Management_CommonMethod.verifyTaskQuestionExport(role,permission);
		DCI_Care_Management_CommonMethod.verifyTaskQuestionOption(role,permission);
		DCI_Care_Management_CommonMethod.verifyTaskQuestionsValidations(role, permission);
		DCI_Care_Management_CommonMethod.verifyTaskQuestionFilters(role, permission);		
		DCI_Care_Management_CommonMethod.verifyEditTaskQuestion(role, permission, questionName);
		DCI_Care_Management_CommonMethod.verifyEditandDeleteTaskQuestion(role, permission);
				
		/*********************************    Check for Supervisor    *********************************/
		System.out.println("* TestCase running for SUPERVISOR Role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR Role ");
	    String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
	    String employeeFirstName="Mike";
	    String employeeLastName="Rose".concat(Utility.UniqueString(3).toLowerCase());
	    String uniqueEmployeeName=employeeFirstName +" "+employeeLastName;
	    DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	    role="Supervisor";
	    DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueEmployeeName, uniqueCostCenterName, "Supervisor");
	    DCI_Care_Management_CommonMethod.verifyTaskQuestionOtherUserLogin(employeeFirstName.concat(employeeLastName),role,permission);
            
	    /*********************************    Check for Employer    *********************************/
	    System.out.println("* TestCase running for EMPLOYER Role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER Role ");
	    employeeFirstName="Mike";
	    employeeLastName="Rose".concat(Utility.UniqueString(3).toLowerCase());
	    uniqueEmployeeName=employeeFirstName +" "+employeeLastName;
	    DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	    role="Employer";
	    DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueEmployeeName, uniqueCostCenterName, "Employer");
	    DCI_Care_Management_CommonMethod.verifyTaskQuestionOtherUserLogin(employeeFirstName.concat(employeeLastName), role,permission);
      
	    /*********************************    Check for Employee without POC permission    *********************************/
		System.out.println("* TestCase running for EMPLOYEE  without POC permission*");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE  without POC permission ");
	    employeeFirstName="Mike";
	    employeeLastName="Rose".concat(Utility.UniqueString(3).toLowerCase());
	    uniqueEmployeeName=employeeFirstName +" "+employeeLastName;
	    DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	    role="Employee";
	    DCI_Care_Management_CommonMethod.verifyTaskQuestionOtherUserLogin(employeeFirstName.concat(employeeLastName), role,permission);
           
	    /*********************************    Check for Employee with POC permission    *********************************/
		System.out.println("* TestCase running for EMPLOYEE  with POC permission*");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE  with POC permission ");
	    employeeFirstName="Mike";
	    employeeLastName="Rose".concat(Utility.UniqueString(3).toLowerCase());
       	uniqueEmployeeName=employeeFirstName +" "+employeeLastName;
       	DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
       	role="Employee";
       	permission="Plan of Care";
       	DCI_Care_Management_CommonMethod.editEmployeeVerifyEnablePlanOfCare(uniqueEmployeeName, employeeFirstName, employeeLastName, true);
       	DCI_Care_Management_CommonMethod.verifyTaskQuestionOtherUserLogin(employeeFirstName.concat(employeeLastName), role,permission);
	}
	
	@Test(priority=34)
	public void Care_Management_TC034() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC034 (DCI-767)", "Verify Enable Care Management");
		System.out.println("[INFO]--> Care_Management_TC034 (DCI-767) - TestCase Execution Begins");
				
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaimssh" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kkk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
 		
 		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    String roleType = "SuperUser";	    
	    DCI_Care_Management_CommonMethod.enableCareManagementValidations(roleType);
	        
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		DCI_Care_Management_CommonMethod.enableCareManagementValidationsForOtherUser(uniqueemployeeName.replace(" ", ""),roleType);
				
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		DCI_Care_Management_CommonMethod.enableCareManagementValidationsForOtherUser(uniqueemployeeName.replace(" ", ""),roleType);
		
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		DCI_Care_Management_CommonMethod.enableCareManagementValidationsForOtherUser(uniqueemployeeName.replace(" ", ""),roleType);		
	}

	
	@Test(priority=36)
    public void Care_Management_TC036() throws Throwable 
    {
	    //isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger=report.startTest("Care_Management_TC036 (DCI-956, DCI-957)", "Visibility of Overview tab, Overview Listing Page");	
		System.out.println("[Info]--> Care_Management_TC036 (DCI-956, DCI-957) - TestCase Execution Begins");
		String permission="No permission";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);

		/*********************************    Check for Superuser    *********************************/
	    System.out.println("* TestCase running for Superuser Role *");
	    logger.log(LogStatus.INFO, " TestCase running for Superuser Role ");
		//************ Create New Client ************//
	    String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
						
		//************ Edit Client ************//
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		DCI_Care_Management_CommonMethod.verifyOverviewTab(uniqueClientName,permission,uniqueCostCenterName); 
		
		/*********************************    Check for Supervisor    *********************************/
		System.out.println("* TestCase running for Supervisor Role *");
	    logger.log(LogStatus.INFO, " TestCase running for Supervisor Role ");
	    //************ Create New Client ************//
	  	clientFirstName = "Steven";
	  	clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	  	uniqueClientName = clientFirstName+ " " +clientLastName;
	  	DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
	  						
	  	//************ Edit Client ************//
	  	DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
	  	DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		
	    String employeeFirstName = "Mean";
		String employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");
		DCI_Care_Management_CommonMethod.verifyOverviewTabForOtherUsers(employeeFirstName.concat(employeeLastName),uniqueClientName,permission,uniqueCostCenterName);
	    
	    /*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER Role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER Role ");
	    //************ Create New Client ************//
	  	clientFirstName = "Steven";
	  	clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	  	uniqueClientName = clientFirstName+ " " +clientLastName;
	  	DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
	  						
	  	//************ Edit Client ************//
	  	DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
	  	DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
	    employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		DCI_Care_Management_CommonMethod.verifyOverviewTabForOtherUsers(employeeFirstName.concat(employeeLastName),uniqueClientName,permission,uniqueCostCenterName);
	    
	    /**********************************     Check for Employee without POC permission   ****************************************/
		System.out.println("* TestCase running for EMPLOYEE Role without poc permission *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE Role ");
	    //************ Create New Client ************//
	  	clientFirstName = "Steven";
	  	clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	  	uniqueClientName = clientFirstName+ " " +clientLastName;
	  	DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
	  						
	  	//************ Edit Client ************//
	  	DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
	  	DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
	    permission="Plan of Care";
	    employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.verifyOverviewTabForOtherUsers(employeeFirstName.concat(employeeLastName),uniqueClientName,permission,uniqueCostCenterName);
	    
	    /**********************************     Check for Employee with POC permission    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE Role with poc permission *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE Role with poc permission ");
	    //************ Create New Client ************//
	  	clientFirstName = "Steven";
	  	clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	  	uniqueClientName = clientFirstName+ " " +clientLastName;
	  	DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
	  						
	  	//************ Edit Client ************//
	  	DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
	  	DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
	    employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, permission, true);
		DCI_Care_Management_CommonMethod.verifyOverviewTabForOtherUsers(employeeFirstName.concat(employeeLastName),uniqueClientName,permission,uniqueCostCenterName);
    }
	
	@Test(priority=37)
    public void Care_Management_TC037() throws Throwable 
    {
	    //isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger=report.startTest("Care_Management_TC037 (DCI-962)", "Task Results tab on overview detail page");	
		System.out.println("[Info]--> Care_Management_TC037 (DCI-962) - TestCase Execution Begins");		
		String uniqueCostCenterName= "Cost_Center"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);	
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);						
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");
		
		/*********************************    Check for Superuser    *********************************/
		System.out.println("* TestCase running for Superuser Role *");
	    logger.log(LogStatus.INFO, " TestCase running for Superuser Role "); 
		//************ Create New Client ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
						
		//************ Edit Client ************//
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");
		//***** Comman Data *****//
		int punchUnit=1; 
	    int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10"; 
		String payRate="10";
		
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(uniqueClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(uniqueClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		//***** Employee Service Account *****//
	    DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		String uniqueGoalName="Goal-".concat(Utility.UniqueString(3).toLowerCase());
		DCI_Care_Management_CommonMethod.createGoal(uniqueClientName, uniqueGoalName, true, "No");
		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combinedData=uniqueClientName +" " +uniqueGoalName+" " +uniqueTaskName+" " +uniqueemployeeName;
	    System.out.println(combinedData); 
	    DCI_Care_Management_CommonMethod.createTask(combinedData, true,"Yes"); 
	    DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(uniqueClientName,uniqueServiceCodeName,uniqueGoalName);
	    //***** Punch Entry *****//
	    String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
	  	String punchId= punchIDOrGetMonth[0].toString();   
	  
	  	String roleType = "SuperUser";
	  	String [] taskResultInformation=DCI_Care_Management_CommonMethod.verifyAddTaskResult(uniqueClientName, uniqueTaskName, punchId, roleType);
	  	String taskResultID=taskResultInformation[1];
	  	combinedData=uniqueClientName+" "+uniqueGoalName+" "+uniqueTaskName+" " +uniqueemployeeName+" " +roleType;
	    DCI_Care_Management_CommonMethod.verifyOverviewTaskResultTab(combinedData, uniqueCostCenterName,taskResultID);
	  	DCI_Care_Management_CommonMethod.verifyOverviewTaskResultExport(uniqueClientName,uniqueCostCenterName, roleType);
	  	
	  	/*********************************    Check for Supervisor    *********************************/
		System.out.println("* TestCase running for Supervisor Role *");
	    logger.log(LogStatus.INFO, " TestCase running for Supervisor Role ");
	    //************ Create New Client ************//
	    clientFirstName = "Steven";
	    clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	    uniqueClientName = clientFirstName+ " " +clientLastName;
	    DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");				
	  	//************ Edit Client ************//
	  	DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
	  	/** EMPLOYEE 1 **/
	  	employeeFirstName = "Mean";
	   	employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
	   	uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
	    DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	    roleType = "Supervisor";
	    DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, roleType);
	    DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");
	    //***** Authorization *****//
	    AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(uniqueClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(uniqueClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		//***** Employee Service Account *****//
	    DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		uniqueGoalName="Goal-".concat(Utility.UniqueString(3).toLowerCase());
		DCI_Care_Management_CommonMethod.createGoal(uniqueClientName, uniqueGoalName, true, "No");
		uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
		combinedData=uniqueClientName +" " +uniqueGoalName+" " +uniqueTaskName+" " +uniqueemployeeName;
	    System.out.println(combinedData); 
	    DCI_Care_Management_CommonMethod.createTask(combinedData, true,"Yes"); 
	    DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(uniqueClientName,uniqueServiceCodeName,uniqueGoalName);
	    //***** Punch Entry *****//
	    punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
	  	punchId= punchIDOrGetMonth[0].toString();   
	   
	   	taskResultInformation=DCI_Care_Management_CommonMethod.verifyAddTaskResult(uniqueClientName, uniqueTaskName, punchId, roleType);
	    taskResultID=taskResultInformation[1];
	  	combinedData=uniqueClientName+" "+uniqueGoalName+" "+uniqueTaskName+" " +uniqueemployeeName+" " +roleType;
	  	DCI_Care_Management_CommonMethod.verifyOverviewTaskResultTabForOtherRoles(employeeFirstName.concat(employeeLastName),combinedData,uniqueCostCenterName,taskResultID);
	  	
	  	/*********************************    Check for Employer    *********************************/
		System.out.println("* TestCase running for Employer Role *");
	    logger.log(LogStatus.INFO, " TestCase running for Employer Role ");
	    //************ Create New Client ************//
	    clientFirstName = "Steven";
	    clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	    uniqueClientName = clientFirstName+ " " +clientLastName;
	    DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");				
	  	//************ Edit Client ************//
	  	DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
	  	/** EMPLOYEE 1 **/
	  	employeeFirstName = "Mean";
	   	employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
	   	uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
	    DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	    roleType = "Employer";
	    DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, roleType);
	    DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");
	    //***** Authorization *****//
	    AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(uniqueClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(uniqueClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		//***** Employee Service Account *****//
	    DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		uniqueGoalName="Goal-".concat(Utility.UniqueString(3).toLowerCase());
		DCI_Care_Management_CommonMethod.createGoal(uniqueClientName, uniqueGoalName, true, "No");
		uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
		combinedData=uniqueClientName +" " +uniqueGoalName+" " +uniqueTaskName+" " +uniqueemployeeName;
	    System.out.println(combinedData); 
	    DCI_Care_Management_CommonMethod.createTask(combinedData, true,"Yes"); 
	    DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(uniqueClientName,uniqueServiceCodeName,uniqueGoalName);
	    //***** Punch Entry *****//
	    punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
	  	punchId= punchIDOrGetMonth[0].toString();   
	  
	   	taskResultInformation=DCI_Care_Management_CommonMethod.verifyAddTaskResult(uniqueClientName, uniqueTaskName, punchId, roleType);
	    taskResultID=taskResultInformation[1];
	  	combinedData=uniqueClientName+" "+uniqueGoalName+" "+uniqueTaskName+" " +uniqueemployeeName+" " +roleType;
	  	DCI_Care_Management_CommonMethod.verifyOverviewTaskResultTabForOtherRoles(employeeFirstName.concat(employeeLastName),combinedData,uniqueCostCenterName,taskResultID);
    }
    
	@Test(priority=38)
    public void Care_Management_TC038() throws Throwable 
    {
	    //isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger=report.startTest("Care_Management_TC038 (DCI-963)", "Care Notes tab on overview detail page");	
		System.out.println("[Info]--> Care_Management_TC038 (DCI-963) - TestCase Execution Begins");		
		String uniqueCostCenterName= "Cost_Center"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);	
		String uniqueFundingSourceName="FundingSrc_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);						
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");
		
		/*********************************    Check for Superuser    *********************************/
		System.out.println("* TestCase running for Superuser Role *");
	    logger.log(LogStatus.INFO, " TestCase running for Superuser Role ");
		//************ Create New Client ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
						
		//************ Edit Client ************//
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");
		//***** Comman Data *****//
		int punchUnit=1; 
	    int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10"; 
		String payRate="10";
		
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(uniqueClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		//DCI_Care_Management_CommonMethod.BalanceVerification(uniqueClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");*/
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		String uniqueGoalName="Goal-".concat(Utility.UniqueString(3).toLowerCase());
		DCI_Care_Management_CommonMethod.createGoal(uniqueClientName, uniqueGoalName, true, "No");
		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combinedData=uniqueClientName +" " +uniqueGoalName+" " +uniqueTaskName+" " +uniqueemployeeName;
	    System.out.println(combinedData); 
	    DCI_Care_Management_CommonMethod.createTask(combinedData, true,"Yes"); 
	    DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(uniqueClientName,uniqueServiceCodeName,uniqueGoalName);
	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
	  	String punchId= punchIDOrGetMonth[0].toString();   
	 
	  	String roleType = "SuperUser";
	  	String [] taskResultInformation=DCI_Care_Management_CommonMethod.verifyAddTaskResult(uniqueClientName, uniqueTaskName, punchId, roleType);
	  	String careNoteID=taskResultInformation[0];
	 
	  	combinedData=uniqueClientName+" "+uniqueGoalName+" "+uniqueTaskName+" " +uniqueemployeeName+" " +roleType;
	  	DCI_Care_Management_CommonMethod.careNotePublish(careNoteID,combinedData);
		String logInEmployeeName=DCI_Care_Management_CommonMethod.getLoggedInEmployeeName();
	  	DCI_Care_Management_CommonMethod.verifyCareNotesTab(combinedData,careNoteID,logInEmployeeName);
	  	DCI_Care_Management_CommonMethod.verifyOverviewCareNotesExport(uniqueClientName);
	  	
	  	//Code for Supervisor and Employer
	  	String[] roles = {"Supervisor","Employer"};
		for (int i=0 ; i<roles.length ; i++)
		{	
			System.out.println("*** TestCase running for "+roles[i]+ "****");
			logger.log(LogStatus.INFO, "*** TestCase running for "+roles[i]+ "***");
			DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, roles[i]);
			DCI_Care_Management_CommonMethod.verifyCareNotesTabForOtherUsers(employeeFirstName.concat(employeeLastName),combinedData,careNoteID,logInEmployeeName);
			DCI_Care_Management_CommonMethod.revokeRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, roles[i]);
		}
	}
	
	@Test(priority=39)
	public void Care_Management_TC039() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC039 (DCI-944)", "Verify Automatic Care Note");
		System.out.println("[INFO]--> Care_Management_TC039 (DCI-944) - TestCase Execution Begins");
				
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaimshn" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
 		int punchUnit=2; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10"; 
		String payRate="10"; 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
		
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    System.out.println("Edit Client Name : "+EditClientName);
	    System.out.println("Employee Name : "+uniqueemployeeName);
	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntryForPreviousDate(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();    
		
		System.out.println("Punch Id : "+punchId);
		
		DCI_Care_Management_CommonMethod.verifyCareNoteForPreviousDatePunch(EditClientName,uniqueemployeeName,roleType);
			
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueemployeeName;
		
		
		DCI_Care_Management_CommonMethod.verifyCareNoteForPreviousDatePunchForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
	
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueemployeeName;		
		DCI_Care_Management_CommonMethod.verifyCareNoteForPreviousDatePunchForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
					
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueemployeeName;
		DCI_Care_Management_CommonMethod.verifyCareNoteForPreviousDatePunchForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
	}
		
	@Test(priority=40)
	public void Care_Management_TC040() throws Throwable 
	{
		isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC040 (DCI- 823)", "Employees tab on Goal details page");
		System.out.println("[INFO]--> Care_Management_TC040 (DCI- 823) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);	
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		int punchUnit=1, monthBack=2, intitialBalance=10000;
		String BurdenMultiplier="0.10";
		String payRate="10";
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//remaining balance=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    String roleType = "SuperUser";
		
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();    
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		DCI_Care_Management_CommonMethod.verifyEmployeeTabGoalDetailsPage(EditClientName, goalName, uniqueTaskName, uniqueemployeeName);
	}
		
	@Test(priority=41)
	public void Care_Management_TC041() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC041 (DCI-918)", "Verify Attachments tab on Task Results details page");
		System.out.println("[INFO]--> Care_Management_TC041 (DCI-918) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//	
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10"; 
		String payRate="10"; 
			
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    String roleType = "SuperUser";
		
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		
	    
	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();      
		
		String[] careNoteAndTaskResultId = DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		String careNoteId = careNoteAndTaskResultId[0];
		String taskResultId = careNoteAndTaskResultId[1];
		String combineIdAndGoal = careNoteId+"-"+taskResultId+"-"+goalName+"-"+uniqueTaskName;
		
		String attachmentName = DCI_Care_Management_CommonMethod.verifyAddAttachmentsTabOnTaskResultDetailsPage(EditClientName,punchId,roleType,combineIdAndGoal);
		
		combined = careNoteId+"-"+taskResultId+"-"+goalName+"-"+uniqueTaskName+"-"+punchId+"-"+EditClientName;
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabSearchFunctionalityOnTaskResultDetailsPage(combined,roleType,attachmentName);
	    
	  
		/********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combined = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabSearchFunctionalityOnTaskResultDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combined);
			
	
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combined = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabSearchFunctionalityOnTaskResultDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combined);
			
					
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combined = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		
		DCI_Care_Management_CommonMethod.verifyAttachmentsTabSearchFunctionalityOnTaskResultDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combined);
	}
	
	@Test(priority=42)
	public void Care_Management_TC042() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC042 (DCI-945)", "Verify Pending care notes");
		System.out.println("[INFO]--> Care_Management_TC042 (DCI-945) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaimshn" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10"; 
		String payRate="10"; 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
	
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();     
		
		String combineData = EditClientName+":"+uniqueemployeeName+":"+uniqueTaskName+":"+punchId;
		
		DCI_Care_Management_CommonMethod.verifyPendingCareNotes(combineData, roleType);
		
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName;
		DCI_Care_Management_CommonMethod.verifyPendingCareNotesForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
					
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName;
		DCI_Care_Management_CommonMethod.verifyPendingCareNotesForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
								
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName;
		DCI_Care_Management_CommonMethod.verifyPendingCareNotesForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);			
	}
	
	@Test(priority=43)
	public void Care_Management_TC043() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC043 (DCI-946)", "Verify Add care note detail page");
		System.out.println("[INFO]--> Care_Management_TC043 (DCI-946) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaimshn" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10"; 
		String payRate="10"; 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
	
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();     
		
		String combineData = EditClientName+":"+uniqueemployeeName+":"+uniqueTaskName+":"+punchId;
		
		DCI_Care_Management_CommonMethod.verifyAddCareNoteDetailPage(combineData, roleType);
			
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName;
		DCI_Care_Management_CommonMethod.verifyAddCareNoteDetailPageForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
					
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName;
		DCI_Care_Management_CommonMethod.verifyAddCareNoteDetailPageForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
								
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName;
		DCI_Care_Management_CommonMethod.verifyAddCareNoteDetailPageForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);			
	}
		
	@Test(priority=44)
	public void Care_Management_TC044() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC044 (DCI-949)", "Verify Publish care note");
		System.out.println("[INFO]--> Care_Management_TC044 (DCI-949) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaimshn" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10"; 
		String payRate="10"; 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
	
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		
	    
	    String combineData = EditClientName+":"+uniqueemployeeName+":"+uniqueTaskName+":"+goalName;
	    DCI_Care_Management_CommonMethod.editTaskForSetRequiredYesNo(combineData, roleType,true);

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();     
		
		combineData = EditClientName+":"+uniqueemployeeName+":"+uniqueTaskName+":"+punchId;
	
		DCI_Care_Management_CommonMethod.verifyPublishCareNote(combineData, roleType, true);
		
		combineData = EditClientName+":"+uniqueemployeeName+":"+uniqueTaskName+":"+goalName;
		DCI_Care_Management_CommonMethod.editTaskForSetRequiredYesNo(combineData, roleType,false);
		
		combineData = EditClientName+":"+uniqueemployeeName+":"+uniqueTaskName+":"+punchId;
		
		DCI_Care_Management_CommonMethod.verifyPublishCareNote(combineData, roleType, false);
		
		
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		DCI_Care_Management_CommonMethod.verifyPublishCareNoteForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
					
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		DCI_Care_Management_CommonMethod.verifyPublishCareNoteForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
								
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		DCI_Care_Management_CommonMethod.verifyPublishCareNoteForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
	}		
	
	@Test(priority=45)
	public void Care_Management_TC045() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC045 (DCI-953)", "Attachment Tab on care note detail page");
		System.out.println("[INFO]--> Care_Management_TC045 (DCI-953) - TestCase Execution Begins");
				
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Client";
		String clientLastName = "Ro" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Fan";
		String EditclientLastName = "Le" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Sup";
 		String employeeLastName = "G" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		int punchUnit=1, monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10", payRate="10";
			
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("*** TestCase running for SUPERUSER ****");
	    logger.log(LogStatus.INFO, "*** TestCase running for SUPERUSER ***");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined = EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();     
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		DCI_Care_Management_CommonMethod.verifyAttachmentTabCareNoteDetailsPage(EditClientName, uniqueemployeeName, punchId, "Superuser");
		
	    /********************************    Check for SUPERVISOR    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyAttachmentTabCareNoteDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
	
		/*********************************    Check for EMPLOYER      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyAttachmentTabCareNoteDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
					
		/**********************************     Check for EMPLOYEE    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
		
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyAttachmentTabCareNoteDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);		
	}
	
	@Test(priority=46)
	public void Care_Management_TC046() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC046 (DCI-954)", "Events Tab on care note detail page");
		System.out.println("[INFO]--> Care_Management_TC046 (DCI-954) - TestCase Execution Begins");
				
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Client";
		String clientLastName = "Ro" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Fan";
		String EditclientLastName = "Le" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);

		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Sup";
 		String employeeLastName = "G" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		int punchUnit=1, monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10", payRate="10";
			
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("*** TestCase running for SUPERUSER ****");
	    logger.log(LogStatus.INFO, "*** TestCase running for SUPERUSER ***");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined = EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();     
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		DCI_Care_Management_CommonMethod.verifyEventsTabCareNoteDetailsPage(EditClientName, uniqueemployeeName, punchId,"Superuser");
	}
	
	@Test(priority=47)
	public void Care_Management_TC047() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC047 (DCI-955)", "Task Result Tab on care note detail page");
		System.out.println("[INFO]--> Care_Management_TC047 (DCI-955) - TestCase Execution Begins");
				
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Client";
		String clientLastName = "Ro" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Fan";
		String EditclientLastName = "Le" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);

		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Sup";
 		String employeeLastName = "G" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		int punchUnit=1, monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10", payRate="10";
			
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("*** TestCase running for SUPERUSER ****");
	    logger.log(LogStatus.INFO, "*** TestCase running for SUPERUSER ***");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined = EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();     
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		DCI_Care_Management_CommonMethod.verifyTaskResultsTabCareNoteDetailsPage(EditClientName, uniqueemployeeName, punchId, "Superuser");
	
		 /********************************    Check for SUPERVISOR    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyTaskResultsTabCareNoteDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
	
		/*********************************    Check for EMPLOYER      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyTaskResultsTabCareNoteDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
					
		/**********************************     Check for EMPLOYEE    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
		
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyTaskResultsTabCareNoteDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);	
	}

	@Test(priority=48)
	public void Care_Management_TC048() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC048 (DCI-950)", "Events tab on add care note detail page");
		System.out.println("[INFO]--> Care_Management_TC048 (DCI-950) - TestCase Execution Begins");
				
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Client";
		String clientLastName = "Ro" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Fan";
		String EditclientLastName = "Le" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);

		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Sup";
 		String employeeLastName = "G" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		int punchUnit=1, monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10", payRate="10";
			
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("*** TestCase running for SUPERUSER ****");
	    logger.log(LogStatus.INFO, "*** TestCase running for SUPERUSER ***");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined = EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();     
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		
		DCI_Care_Management_CommonMethod.verifyEventsTabAddCareNotesPage(EditClientName, uniqueemployeeName, punchId);
	}

	@Test(priority=49)
	public void Care_Management_TC049() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC049 (DCI-951)", "Verify Publish care note Search Functionality and page layout");
		System.out.println("[INFO]--> Care_Management_TC049 (DCI-951) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaimshn" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10"; 
		String payRate="10"; 
	
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
	
		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();   
		
		String combineData = EditClientName+":"+uniqueemployeeName+":"+uniqueTaskName+":"+punchId;
	
		DCI_Care_Management_CommonMethod.verifyPublishCareNoteSearchFunctionalityAndPageLayout(combineData, roleType);
		
	    /********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		DCI_Care_Management_CommonMethod.verifyPublishCareNoteSearchFunctionalityAndPageLayoutForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
					
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		DCI_Care_Management_CommonMethod.verifyPublishCareNoteSearchFunctionalityAndPageLayoutForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
								
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combineData = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		DCI_Care_Management_CommonMethod.verifyPublishCareNoteSearchFunctionalityAndPageLayoutForOther(uniqueemployeeName.replace(" ", ""), roleType,combineData);
				
	}

	@Test(priority=50)
	public void Care_Management_TC050() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC050 (DCI-961)", "Events tab on overview details page");
		System.out.println("[INFO]--> Care_Management_TC050 (DCI-961) - TestCase Execution Begins");
				
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Client";
		String clientLastName = "Ro" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Fan";
		String EditclientLastName = "Le" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);

		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Sup";
 		String employeeLastName = "G" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		int punchUnit=1, monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10", payRate="10";
			
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("*** TestCase running for SUPERUSER ****");
	    logger.log(LogStatus.INFO, "*** TestCase running for SUPERUSER ***");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined = EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();     
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		
		DCI_Care_Management_CommonMethod.verifyEventsTabOverviewPage(EditClientName, uniqueemployeeName, uniqueCostCenterName);
	}
	
	@Test(priority=51)
	public void Care_Management_TC051() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC051 (DCI-919)", "Verify Notes tab on Task Results details page");
		System.out.println("[INFO]--> Care_Management_TC051 (DCI-919) - TestCase Execution Begins");
		
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		//String uniqueCostCenterName= "";
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Mikedit";
		String EditclientLastName = "Glaims" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//	
		int punchUnit=1; 
		int monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10"; 
		String payRate="10"; 
			
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("* TestCase running for SUPERUSER role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
	    String roleType = "SuperUser";
		
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);

		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		
	    
	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();      
		
		String[] careNoteAndTaskResultId = DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		String careNoteId = careNoteAndTaskResultId[0];
		String taskResultId = careNoteAndTaskResultId[1];
		String combineIdAndGoal = careNoteId+"-"+taskResultId+"-"+goalName+"-"+uniqueTaskName;
		
		DCI_Care_Management_CommonMethod.verifyAddNotesTabOnTaskResultDetailsPage(EditClientName,punchId,roleType,combineIdAndGoal);
		
		combined = careNoteId+"-"+taskResultId+"-"+goalName+"-"+uniqueTaskName+"-"+punchId+"-"+EditClientName;
		DCI_Care_Management_CommonMethod.verifyNotesTabSearchFunctionalityOnTaskResultDetailsPage(combined,roleType);
	    
	  
		/********************************    Check for Supervisor    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		combined = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		
		DCI_Care_Management_CommonMethod.verifyNotesTabSearchFunctionalityOnTaskResultDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combined);
			
	
		/*********************************    Check for Employer      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);

		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combined = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		
		DCI_Care_Management_CommonMethod.verifyNotesTabSearchFunctionalityOnTaskResultDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combined);
			
					
		/**********************************     Check for Employee    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		intitialBalance=10000;
		BurdenMultiplier="0.10"; 
		payRate="10"; 
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		//remaing balnce=intitialBalance ,pre-Auth=0,balance=0,status=None
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combined = EditClientName+":"+uniqueServiceCodeName+":"+uniqueCostCenterName+":"+employeeFirstName+":"+employeeLastName+":"+uniqueTaskName+":"+uniqueemployeeName+":"+goalName;
		
		DCI_Care_Management_CommonMethod.verifyNotesTabSearchFunctionalityOnTaskResultDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combined);
							
	}
	
	@Test(priority=52)
	public void Care_Management_TC052() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC052 (DCI-952)", "Notes Tab on care note detail page");
		System.out.println("[INFO]--> Care_Management_TC052 (DCI-952) - TestCase Execution Begins");
				
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);		
		String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Client";
		String clientLastName = "Ro" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		String EditclientFirstName = "Fan";
		String EditclientLastName = "Le" + Utility.UniqueNumber(3).toLowerCase();
		String EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
	
		/** EMPLOYEE 1 **/
		String employeeFirstName = "Sup";
		String employeeLastName = "G" + Utility.UniqueString(4).toLowerCase();
		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		int punchUnit=1, monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10", payRate="10";
			
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
	
		System.out.println("*** TestCase running for SUPERUSER ****");
	    logger.log(LogStatus.INFO, "*** TestCase running for SUPERUSER ***");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(EditClientName);
	
		String goalName = "GOAL-".concat( Utility.UniqueString(4));
		DCI_Care_Management_CommonMethod.createGoal(EditClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
	
		String uniqueTaskName="Task-".concat(Utility.UniqueString(3).toLowerCase());
	    String combined = EditClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		
	
	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();     
		
		DCI_Care_Management_CommonMethod.verifyAddTaskResult(EditClientName, uniqueTaskName, punchId, roleType);
		DCI_Care_Management_CommonMethod.verifyNotesTabCareNoteDetailsPage(EditClientName, uniqueemployeeName, punchId, "Superuser");
		
	    /********************************    Check for SUPERVISOR    ********************************/		
	    System.out.println("* TestCase running for SUPERVISOR role *");
	    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
	    roleType = "Supervisor";
		//************ Create New Client and Edit ************//
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaimss" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
			
		/** EMPLOYEE 1 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		/** Employee given Supervisor role **/
		System.out.println("Creating a Supervisor role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");		
		
		//***** Funding Account *****//
			DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal[0];
		goalName = uniqueTaskNameAndGoal[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		
		String combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyNotesTabCareNoteDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
	
		/*********************************    Check for EMPLOYER      ************************************/
		System.out.println("* TestCase running for EMPLOYER role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
		
	    roleType = "Employer";
		clientFirstName = "Steven";
		clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mike";
		EditclientLastName = "Glaims" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		/** EMPLOYEE 2 **/
		employeeFirstName = "Mean";
		employeeLastName = "K" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	
		/** Employee given Employer role **/
		System.out.println("Creating a Employer role from Employee");
		DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
		
		//***** Funding Account *****//
			DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
	
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal1 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal1[0];
		goalName = uniqueTaskNameAndGoal1[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyNotesTabCareNoteDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);
			
					
		/**********************************     Check for EMPLOYEE    ****************************************/
		System.out.println("* TestCase running for EMPLOYEE role *");
	    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
		
	    roleType = "Employee";
		clientFirstName = "Steven";
		clientLastName = "Rockk" + Utility.UniqueNumber(4).toLowerCase();
		uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
				
		//************ Edit Client ************//
		EditclientFirstName = "Mikecl";
		EditclientLastName = "Glaimshnm" + Utility.UniqueNumber(4).toLowerCase();
		EditClientName = EditclientFirstName+ " " +EditclientLastName;
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, EditclientFirstName, EditclientLastName, true);
		
		//************ Create an Employee ***************//
		employeeFirstName = "Timm";
		employeeLastName = "V" + Utility.UniqueString(4).toLowerCase();
		uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		DCI_Care_Management_CommonMethod.managePermissionEmployee(uniqueemployeeName, "Plan Of Care", true);
		Thread.sleep(1000);		    
		
		//***** Funding Account *****//
			DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, EditClientName, uniqueCostCenterName, "Hourly");
		
		//***** Common Data *****//
		punchUnit=1; 
		monthBack=2;
		intitialBalance=10000;
		BurdenMultiplier="0.10";
		payRate="10";
		
		//***** Authorization *****//
		AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(EditClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(EditClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueemployeeName,"Hourly", EditClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
			
		String[] uniqueTaskNameAndGoal2 = DCI_Care_Management_CommonMethod.addPocGoalTaskForAddCareNotes(uniqueemployeeName.replace(" ", ""), EditClientName,uniqueServiceCodeName,roleType,uniqueCostCenterName,employeeFirstName,employeeLastName);
		uniqueTaskName = uniqueTaskNameAndGoal2[0];
		goalName = uniqueTaskNameAndGoal2[1];
		
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(EditClientName,uniqueServiceCodeName,goalName);
		combineData = EditClientName+"-"+uniqueServiceCodeName+"-"+uniqueCostCenterName+"-"+employeeFirstName+"-"+employeeLastName+"-"+uniqueTaskName;
		DCI_Care_Management_CommonMethod.verifyNotesTabCareNoteDetailsPageForOtherUser(uniqueemployeeName.replace(" ", ""), roleType,combineData);		
	}
	
	@Test(priority=53)
	public void Care_Management_TC053() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC053 (DCI-958)", "Attachments tab on overview details page");
		System.out.println("[INFO]--> Care_Management_TC053 (DCI-958) - TestCase Execution Begins");	
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rockss" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClientForDisableCareManagement(clientFirstName, clientLastName, uniqueCostCenterName, "", ""); 
		
		//************ Edit Client ************//
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		String uniqueAttachmentName=Utility.UniqueString(6);
		DCI_Care_Management_CommonMethod.addNewAttachmentOverview(uniqueClientName,uniqueAttachmentName);
 		DCI_Care_Management_CommonMethod.verifyAttachmentTabOverview(uniqueClientName,uniqueAttachmentName);
		
		/*********************************    Check for Supervisor    *********************************/
 		System.out.println("* TestCase running for Supervisor Role *");
 		logger.log(LogStatus.INFO, " TestCase running for Supervisor Role ");
	    //************ Create New Client ************//
	    clientFirstName = "Steven";
	    clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	   	uniqueClientName = clientFirstName+ " " +clientLastName;
	   	DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");				
	  	//************ Edit Client ************//
	   	DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		String employeeFirstName = "Mean";
	  	String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
	  	String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
	    DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	    String roleType = "Supervisor";
	    DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, roleType);
		DCI_Care_Management_CommonMethod.verifyAttachmentTabOverviewForOtherRoles(employeeFirstName.concat(employeeLastName),uniqueClientName,uniqueAttachmentName);	
		
		/*********************************    Check for Employer    *********************************/
		System.out.println("* TestCase running for Employer Role *");
	    logger.log(LogStatus.INFO, " TestCase running for Employer Role ");
	    //************ Create New Client ************//
	    clientFirstName = "Steven";
	    clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	    uniqueClientName = clientFirstName+ " " +clientLastName;
	    DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");				
	  	//************ Edit Client ************//
	    DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		employeeFirstName = "Mean";
	  	employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
	  	uniqueemployeeName = employeeFirstName + " " + employeeLastName;
	    DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	    roleType = "Employer";
	    DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, roleType);
	    DCI_Care_Management_CommonMethod.verifyAttachmentTabOverviewForOtherRoles(employeeFirstName.concat(employeeLastName),uniqueClientName,uniqueAttachmentName);
	    
	}
	
	@Test(priority=54)
	public void Care_Management_TC054() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC054 (DCI-959)", "Notes Tab on overview detail page");
		System.out.println("[INFO]--> Care_Management_TC054 (DCI-959) - TestCase Execution Begins");	
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Steven";
		String clientLastName = "Rockss" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClientForDisableCareManagement(clientFirstName, clientLastName, uniqueCostCenterName, "", ""); 
		
		//************ Edit Client ************//
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		String roleType = "Superuser";
		
		DCI_Care_Management_CommonMethod.addNewNoteOverview(uniqueClientName);
		
 		DCI_Care_Management_CommonMethod.verifyNoteTabOverview(uniqueClientName,roleType);
		
		/*********************************    Check for Supervisor    *********************************/
 		System.out.println("* TestCase running for Supervisor Role *");
 		logger.log(LogStatus.INFO, " TestCase running for Supervisor Role ");
	    //************ Create New Client ************//
	    clientFirstName = "Steven";
	    clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	   	uniqueClientName = clientFirstName+ " " +clientLastName;
	   	DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");				
	  	//************ Edit Client ************//
	   	DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		String employeeFirstName = "Mean";
	  	String employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
	  	String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
	    DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	    roleType = "Supervisor";
	    DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, roleType);
		DCI_Care_Management_CommonMethod.verifyNoteTabOverviewForOtherRoles(employeeFirstName.concat(employeeLastName),uniqueClientName,roleType);	
		
		/*********************************    Check for Employer    *********************************/
		System.out.println("* TestCase running for Employer Role *");
	    logger.log(LogStatus.INFO, " TestCase running for Employer Role ");
	    //************ Create New Client ************//
	    clientFirstName = "Steven";
	    clientLastName = "Rock" + Utility.UniqueNumber(4).toLowerCase();
	    uniqueClientName = clientFirstName+ " " +clientLastName;
	    DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");				
	  	//************ Edit Client ************//
	    DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
		employeeFirstName = "Mean";
	  	employeeLastName = "Kk" + Utility.UniqueString(4).toLowerCase();
	  	uniqueemployeeName = employeeFirstName + " " + employeeLastName;
	    DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
	    roleType = "Employer";
	    DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, roleType);
	    DCI_Care_Management_CommonMethod.verifyNoteTabOverviewForOtherRoles(employeeFirstName.concat(employeeLastName),uniqueClientName,roleType);
	    
	}
	
	@Test(priority=55)
	public void Care_Management_TC055() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC055 (DCI-1008, DCI-1010, DCI-1011)", "Raw Data Dump Report,Task result Detail Report,Pending Care Notes Report");
		System.out.println("[INFO]--> Care_Management_TC055 (DCI-1008, DCI-1010, DCI-1011) - TestCase Execution Begins");
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		String uniqueCostCenterName= "CostCenter"+Utility.UniqueString(3).toLowerCase();
		String costCenterCode=DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);
		
		String uniqueFundingSourceName="FundingSrces"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);
		
		String uniqueServiceCodeName="ServiceCode"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen*/
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Client";
		String clientLastName = "Ro" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		
		//************ Create New Client for verifying  ************//
		String clientFirstName1 = "Client1";
		String clientLastName1= "Ro1" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName1 = clientFirstName1+ " " +clientLastName1;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName1, clientLastName1, uniqueCostCenterName, "", "");
		
		System.out.println("*** TestCase running for SUPERUSER ****");
	    logger.log(LogStatus.INFO, "*** TestCase running for SUPERUSER ***");
	    
	    String roleType = "SuperUser";
	    DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
	    
	    String goalName = "GOAL".concat( Utility.UniqueString(4));
	  	DCI_Care_Management_CommonMethod.createGoal(uniqueClientName, goalName, true, "No");
	  	
	  	String uniqueTaskName="Task".concat(Utility.UniqueString(3).toLowerCase());
	    String combined = uniqueClientName +" " +goalName+" " +uniqueTaskName; 
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");
	    
	    /** EMPLOYEE 1 **/
		String employeeFirstName = "Sup";
		String employeeLastName = "G" + Utility.UniqueString(4).toLowerCase();
		String uniqueEmployeeName = employeeFirstName + " " + employeeLastName;	
		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(uniqueClientName,uniqueServiceCodeName,goalName);
		
		//***** Common Data *****//
		int punchUnit=1, monthBack=2;
		int intitialBalance=10000;
		String BurdenMultiplier="0.10", payRate="10";
			
		//***** Authorization *****//
		String AuthorizationID=DCI_Care_Management_CommonMethod.clientAuthorization(uniqueClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Balance *****//
		DCI_Care_Management_CommonMethod.BalanceVerification(uniqueClientName, uniqueServiceCodeName, "", AuthorizationID, intitialBalance,intitialBalance, 0, 0, "", 0, 0, "Balance verification BeforePunch is entered");
		
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueEmployeeName,"Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);
	    
	    
	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString(); 
		
		String taskResultData[]=DCI_Care_Management_CommonMethod.verifyAddTaskResult(uniqueClientName, uniqueTaskName, punchId, roleType);
		
		String combinedData=DCI_Care_Management_CommonMethod.getPlanOfCareData(uniqueClientName,goalName,uniqueTaskName,taskResultData,punchId);
		
		String comData=costCenterCode+":"+punchId+":"+uniqueClientName1+":"+uniqueEmployeeName+":"+goalName+":"+uniqueTaskName;
		
		DCI_Care_Management_CommonMethod.verifyRawDataDumpReport(combinedData,roleType,comData);
		DCI_Care_Management_CommonMethod.verifyPendingCareNotesReport(combinedData,roleType,comData);
		DCI_Care_Management_CommonMethod.verifyTaskResultDetailReport(combinedData,comData,roleType);
		String[] roles = {"Supervisor","Employer","Employee"};
		
		for (int i=0 ; i<roles.length ; i++)
		{	
			String FILE_PATH = System.getProperty("user.dir")+ "\\Browser_Files\\AccessControl.csv";
		    Thread.sleep(600);			    	
		    
			List<String> csvINFO = ReadDataFromPropertiesFile.readDataLineByLine(FILE_PATH);
			String permission = csvINFO.get(i+1);
			if(roles[i].equalsIgnoreCase("Employee"))
			{
				DCI_Care_Management_CommonMethod.verifyTaskResultDetailReportForOtherRoles(combinedData,employeeFirstName.concat(employeeLastName),permission,roles[i],comData);
			}
			else
			{
				System.out.println("*** TestCase running for "+roles[i]+ "****");
				logger.log(LogStatus.INFO, "*** TestCase running for "+roles[i]+ "***");
				DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueEmployeeName, uniqueCostCenterName, roles[i]);
				DCI_Care_Management_CommonMethod.verifyTaskResultDetailReportForOtherRoles(combinedData,employeeFirstName.concat(employeeLastName),permission,roles[i],comData);
				DCI_Care_Management_CommonMethod.revokeRolesFromEmployee(uniqueEmployeeName, uniqueCostCenterName, roles[i]);
			}
		}	
	}

	@Test(priority=56)
	public void Care_Management_TC056() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC056 (DCI 1079-1084)", "Verify e2e Flow for multiple Roles");
		System.out.println("[INFO]--> Care_Management_TC056 (DCI 1079-1084) - TestCase Execution Begins");
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);		
		String uniqueFundingSourceName="FundingSrcesss_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);		
		String uniqueServiceCodeName="Service_Codess"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");		//Acumen		
		//DCI_Care_Management_CommonMethod.createNewServiceCodeRise(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, "Hourly", "Hourly");				//Rise
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Mars";
		String clientLastName = "Ven" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Top" + Utility.UniqueString(4).toLowerCase();
 		String uniqueEmployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");
		
		//***** Comman Data *****//
		int punchUnit=1, monthBack=2, intitialBalance=10000;
		String BurdenMultiplier="0.10", payRate="10";
	
		//***** Authorization *****//
		DCI_Care_Management_CommonMethod.clientAuthorization(uniqueClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueEmployeeName,"Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("*****>> Data Created Under SUPERUSER role <<*****");
	    logger.log(LogStatus.INFO, "*****>> Data Created Under SUPERUSER role <<*****");
	    String roleType = "SuperUser";
		
	    String effectiveAndEndDate = DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
	    String [] splitDate = effectiveAndEndDate.split("-");
	    String effectiveDate = splitDate[0];
        String endDate = splitDate[1];    
        
		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(uniqueClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(uniqueClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=uniqueClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();   
		//String getMonth=punchIDOrGetMonth[1].toString();   
		
		String[] careNoteAndTaskResultId = DCI_Care_Management_CommonMethod.verifyAddTaskResult(uniqueClientName, uniqueTaskName, punchId, roleType);
		String careNoteId = careNoteAndTaskResultId[0];
		String taskResultId = careNoteAndTaskResultId[1];
		//String combineIdAndGoal = careNoteId+"-"+taskResultId+"-"+goalName+"-"+uniqueTaskName+"-"+uniqueEmployeeName;
		
		String combineData = uniqueClientName+":"+uniqueEmployeeName+":"+uniqueTaskName+":"+punchId;
		
		DCI_Care_Management_CommonMethod.verifyPublishCareNote(combineData, roleType, false);
		
		/***** Case Worker *****/
 		String caseFirstName = "Doc", caseLastName = "Ant"+Utility.UniqueString(3);
 		DCI_Care_Management_CommonMethod.createCaseWorkersWithFundingSrc(caseFirstName, caseLastName, uniqueCostCenterName, uniqueFundingSourceName);	
 		
 		String superUserName = DCI_Care_Management_CommonMethod.getLoginEmployeeName();
 		
 		
// 		String uniqueCostCenterName = "Cost_Center fti";
// 		String uniqueFundingSourceName = "FundingSrcesss_pnr";
// 		String uniqueServiceCodeName = "Service_Codesskta";
// 		String uniqueClientName = "Mars Ven1416";
// 		String uniqueEmployeeName = "Mean Topwgxc";
// 		String effectiveDate = "06/21/2019";
// 		String endDate = "08/02/2019";
// 		String goalName = "GOAL_VNAk";
// 		String uniqueTaskName = "Task_jdb";
// 		String punchId = "6081";
// 		String careNoteId = "424";
// 		String taskResultId = "214";
// 		String caseFirstName = "Doc";
// 		String caseLastName = "AntGJZ";
// 		String superUserName = "Abhishek Verma";
 		
 				;
 		/***** Check for Roles *****/
 		//String[] roles = {"Auditor","Payroll Team","Billing Team","Authorization","View Only","Case Worker"};
 		String[] roles = {"Case Worker"};
 		for(int y=0 ; y<roles.length ; y++ )
 		{
 			System.out.println("\n<<<<<<<<<<< Employee RoleType: "+roles[y]+" >>>>>>>>>>>>>");
 	 		logger.log(LogStatus.PASS, "<<<<<<<<<<< Employee RoleType:  "+roles[y]+"  >>>>>>>>>>>>>");	 			 		 		
 	 		if(!(roles[y].equals("Case Worker"))){	
 	 			DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueEmployeeName, uniqueCostCenterName, roles[y]);	
		 		DCI_Care_Management_CommonMethod.verifyCareManagementModuleOtherUser(uniqueEmployeeName.replace(" ",""), superUserName, uniqueCostCenterName, uniqueClientName, uniqueEmployeeName, effectiveDate, endDate, goalName, uniqueTaskName, punchId, taskResultId, careNoteId, roles[y]);	 		
		 		DCI_Care_Management_CommonMethod.revokeRolesFromEmployee(uniqueEmployeeName, uniqueCostCenterName, roles[y]);
 	 		}else{
 	 			DCI_Care_Management_CommonMethod.verifyCareManagementModuleOtherUser(caseFirstName.concat(caseLastName), superUserName, uniqueCostCenterName, uniqueClientName, uniqueEmployeeName, effectiveDate, endDate, goalName, uniqueTaskName, punchId, taskResultId, careNoteId,"Case Worker");	 			
 	 		}	 			 		
 		}		
 		System.out.println("\n<<<<<<<<<<< Employee Without POC >>>>>>>>>>>>>");
	 	logger.log(LogStatus.PASS, "<<<<<<<<<<< Employee Without POC >>>>>>>>>>>>>");		
 		DCI_Care_Management_CommonMethod.verifyCareManagementModuleOtherUser(uniqueEmployeeName.replace(" ",""), superUserName, uniqueCostCenterName, uniqueClientName, uniqueEmployeeName, effectiveDate, endDate, goalName, uniqueTaskName, punchId, taskResultId, careNoteId, "baseEmployee");	//Without POC
	}

	@Test(priority=57)
	public void Care_Management_TC057() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC057 (DCI-1091)", "Verify e2e Flow for multiple Roles [Sase worker with FundingAccount]");
		System.out.println("[INFO]--> Care_Management_TC057 (DCI-1091) - TestCase Execution Begins");
		
		//************ Create New Cost Center Code || Funding Source Code || Service Code ************//		
		String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);		
		String uniqueFundingSourceName="FundingSrcesss_"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);		
		String uniqueServiceCodeName="Service_Codess"+Utility.UniqueString(3).toLowerCase();
		DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");		//Acumen		
		//DCI_Care_Management_CommonMethod.createNewServiceCodeRise(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, "Hourly", "Hourly");				//Rise
		
		//************ Create New Client and Edit ************//
		String clientFirstName = "Mars";
		String clientLastName = "Ven" + Utility.UniqueNumber(4).toLowerCase();
		String uniqueClientName = clientFirstName+ " " +clientLastName;
		DCI_Care_Management_CommonMethod.createClient(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
		
		//************ Edit Client ************//
		DCI_Care_Management_CommonMethod.editClientVerifyEnableCareMgmt(uniqueClientName, clientFirstName, clientLastName, true);
		
		/** EMPLOYEE 1 **/
 		String employeeFirstName = "Mean";
 		String employeeLastName = "Top" + Utility.UniqueString(4).toLowerCase();
 		String uniqueEmployeeName = employeeFirstName + " " + employeeLastName;	
 		DCI_Care_Management_CommonMethod.createEmployee(employeeFirstName, employeeLastName, uniqueCostCenterName);
 		
 		/***** Case Worker *****/
 		String caseFirstName = "Doc", caseLastName = "Ant"+Utility.UniqueString(3);
 		DCI_Care_Management_CommonMethod.createCaseWorkers(caseFirstName, caseLastName, uniqueCostCenterName);	
 		
		
		//***** Funding Account *****//
 		DCI_Care_Management_CommonMethod.createfundingAccountWithCaseWorker(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly",caseFirstName+" "+caseLastName);
		
		//***** Comman Data *****//
		int punchUnit=1, monthBack=2, intitialBalance=10000;
		String BurdenMultiplier="0.10", payRate="10";;
		//double burdenMultiplier = Double.parseDouble(BurdenMultiplier); 		
		//double payRateInDouble = Double.parseDouble(payRate); 
	
		//***** Authorization *****//
		DCI_Care_Management_CommonMethod.clientAuthorization(uniqueClientName, "Hourly", uniqueFundingSourceName, uniqueServiceCodeName, "Units",intitialBalance);
		
		//***** Employee Service Account *****//
		DCI_Care_Management_CommonMethod.createEmployeeServiceAccount(uniqueEmployeeName,"Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName, "Units",BurdenMultiplier,payRate);

		System.out.println("*****>> Data Created Under SUPERUSER role <<*****");
	    logger.log(LogStatus.INFO, "*****>> Data Created Under SUPERUSER role <<*****");
	    String roleType = "SuperUser";
		
	    String effectiveAndEndDate = DCI_Care_Management_CommonMethod.createNewPlanOfCare(uniqueClientName);
	    String [] splitDate = effectiveAndEndDate.split("-");
	    String effectiveDate = splitDate[0];
        String endDate = splitDate[1];    
        
		String goalName = "GOAL_".concat( Utility.UniqueString(4));
		Thread.sleep(500);
		DCI_Care_Management_CommonMethod.createGoal(uniqueClientName, goalName, true, "No"); 
	
		DCI_Care_Management_CommonMethod.goalLinkedWithFundingAccount(uniqueClientName,uniqueServiceCodeName,goalName);

		String uniqueTaskName="Task_".concat(Utility.UniqueString(3).toLowerCase());
	    String combined=uniqueClientName +" " +goalName+" " +uniqueTaskName;
	    System.out.println(combined);
	    DCI_Care_Management_CommonMethod.createTask(combined, true,"Yes");		

	    //***** Punch Entry *****//
		String[] punchIDOrGetMonth=DCI_Care_Management_CommonMethod.createPunchEntry(punchUnit, monthBack, "Hourly", uniqueClientName, uniqueCostCenterName, "", "", "", "", uniqueServiceCodeName,employeeFirstName.concat(employeeLastName),"");
		String punchId= punchIDOrGetMonth[0].toString();   
		//String getMonth=punchIDOrGetMonth[1].toString();   
		
		String[] careNoteAndTaskResultId = DCI_Care_Management_CommonMethod.verifyAddTaskResult(uniqueClientName, uniqueTaskName, punchId, roleType);
		String careNoteId = careNoteAndTaskResultId[0];
		String taskResultId = careNoteAndTaskResultId[1];
		//String combineIdAndGoal = careNoteId+"-"+taskResultId+"-"+goalName+"-"+uniqueTaskName+"-"+uniqueEmployeeName;
		
		String combineData = uniqueClientName+":"+uniqueEmployeeName+":"+uniqueTaskName+":"+punchId;
		
		DCI_Care_Management_CommonMethod.verifyPublishCareNote(combineData, roleType, false);
		
 		String superUserName = DCI_Care_Management_CommonMethod.getLoginEmployeeName();
 		
 		DCI_Care_Management_CommonMethod.verifyCareManagementModuleOtherUser(caseFirstName.concat(caseLastName), superUserName, uniqueCostCenterName, uniqueClientName, uniqueEmployeeName, effectiveDate, endDate, goalName, uniqueTaskName, punchId, taskResultId, careNoteId,"Case Worker");
	}
	
	@Test(priority=99)
	public void Care_Management_TC099() throws Throwable 
	{
		//isConditionX(ReadDataFromPropertiesFile.readTxtFileBoolean(testName, S3BucketConfigFilePath));
		logger =report.startTest("Care_Management_TC099 (DCI-768)", "Verify Disable Care Management Tab");
		System.out.println("[INFO]--> Care_Management_TC099 (DCI-768) - TestCase Execution Begins");
				
		DCI_Care_Management_CommonMethod.modifyInstanceLevelSettingsAdminDashboard("Features_CareManagement_IsEnabled", false, "0");
		for(int i=0 ; i<8 ; i++ )
		{
			Thread.sleep(4000);		
			System.out.println("Waiting for server to restart...");
		}
		driver.navigate().refresh();	
		boolean status = LoginPage.login(AGlobalComponents.username,AGlobalComponents.recoveryPassword);		
		if(status==true) 
		{
			//************ Create New Cost Center Code || Funding Source Code || Service Code ************//				
			String uniqueCostCenterName= "Cost_Center "+Utility.UniqueString(3).toLowerCase();
			DCI_Care_Management_CommonMethod.createCostCenter(uniqueCostCenterName);		
			String uniqueFundingSourceName="FundingSrces_"+Utility.UniqueString(3).toLowerCase();
			DCI_Care_Management_CommonMethod.createFundingSource(uniqueFundingSourceName, uniqueCostCenterName);			
			String uniqueServiceCodeName="Service_Code"+Utility.UniqueString(3).toLowerCase();
			DCI_Care_Management_CommonMethod.createNewServiceCodeAcumen(uniqueFundingSourceName,"Units","Hourly", uniqueServiceCodeName, "Hourly", "Hourly");	//Acumen
			
			//************ Create New Client and Edit ************//
			String clientFirstName = "Steven";
			String clientLastName = "Rockss" + Utility.UniqueNumber(4).toLowerCase();
			String uniqueClientName = clientFirstName+ " " +clientLastName;
			DCI_Care_Management_CommonMethod.createClientForDisableCareManagement(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
			
			//************ Edit Client ************//
			DCI_Care_Management_CommonMethod.editClientToVerifyEnableCareMgmtNotPresentForDisableCareManagement(uniqueClientName, clientFirstName, clientLastName);
												
			//************ EMPLOYEE 1 ************//
	 		String employeeFirstName = "Mean";
	 		String employeeLastName = "Kkkk" + Utility.UniqueString(4).toLowerCase();
	 		String uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
	 		DCI_Care_Management_CommonMethod.createEmployeeForDisableCareManagement(employeeFirstName, employeeLastName, uniqueCostCenterName);
	 		
	 		//************ Funding Account ************//
	 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");
	 		
	 		System.out.println("* TestCase running for SUPERUSER role *");
		    logger.log(LogStatus.INFO, " TestCase running for SUPERUSER role ");
		    String roleType = "SuperUser";	    
		    DCI_Care_Management_CommonMethod.disableCareManagementValidations(roleType, uniqueemployeeName, uniqueClientName);		    		    
			//DCI_Care_Management_CommonMethod.verifyCareManagementOffImpact(uniqueClientName,"Superuser");  
		    
		    /********************************    Check for Supervisor    ********************************/		
		    System.out.println("* TestCase running for SUPERVISOR role *");
		    logger.log(LogStatus.INFO, " TestCase running for SUPERVISOR role ");
		    roleType = "Supervisor";
			//************ Create New Client and Edit ************//
			clientFirstName = "Steven";
			clientLastName = "Rockkks" + Utility.UniqueNumber(4).toLowerCase();
			uniqueClientName = clientFirstName+ " " +clientLastName;
			DCI_Care_Management_CommonMethod.createClientForDisableCareManagement(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
					
			//************ Edit Client ************//
			DCI_Care_Management_CommonMethod.editClientToVerifyEnableCareMgmtNotPresentForDisableCareManagement(uniqueClientName, clientFirstName, clientLastName);
				
			/** EMPLOYEE 1 **/
			employeeFirstName = "Mean";
			employeeLastName = "Kss" + Utility.UniqueString(4).toLowerCase();
			uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
			DCI_Care_Management_CommonMethod.createEmployeeForDisableCareManagement(employeeFirstName, employeeLastName, uniqueCostCenterName);
			
			/** Employee given Supervisor role **/
			System.out.println("Creating a Supervisor role from Employee");
			DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Supervisor");	
			
			//***** Funding Account *****//
	 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");
			DCI_Care_Management_CommonMethod.disableCareManagementValidationsForOtherUser(uniqueemployeeName.replace(" ", ""),roleType,uniqueemployeeName, uniqueClientName);
					
			/*********************************    Check for Employer      ************************************/
			System.out.println("* TestCase running for EMPLOYER role *");
		    logger.log(LogStatus.INFO, " TestCase running for EMPLOYER role ");
			
		    roleType = "Employer";
			clientFirstName = "Steven";
			clientLastName = "Rockks" + Utility.UniqueNumber(4).toLowerCase();
			uniqueClientName = clientFirstName+ " " +clientLastName;
			DCI_Care_Management_CommonMethod.createClientForDisableCareManagement(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
					
			//************ Edit Client ************//
			DCI_Care_Management_CommonMethod.editClientToVerifyEnableCareMgmtNotPresentForDisableCareManagement(uniqueClientName, clientFirstName, clientLastName);
			
			/** EMPLOYEE 2 **/
			employeeFirstName = "Mean";
			employeeLastName = "Kkks" + Utility.UniqueString(4).toLowerCase();
			uniqueemployeeName = employeeFirstName + " " + employeeLastName;
			DCI_Care_Management_CommonMethod.createEmployeeForDisableCareManagement(employeeFirstName, employeeLastName, uniqueCostCenterName);
	
			/** Employee given Employer role **/
			System.out.println("Creating a Employer role from Employee");
			DCI_Care_Management_CommonMethod.manageRolesFromEmployee(uniqueemployeeName, uniqueCostCenterName, "Employer");
			
			/***** Funding Account *****/
	 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");
			DCI_Care_Management_CommonMethod.disableCareManagementValidationsForOtherUser(uniqueemployeeName.replace(" ", ""),roleType,uniqueemployeeName, uniqueClientName);
			
			/**********************************     Check for Employee    ****************************************/
			System.out.println("* TestCase running for EMPLOYEE role *");
		    logger.log(LogStatus.INFO, " TestCase running for EMPLOYEE role ");
			
		    roleType = "Employee";
			clientFirstName = "Steven";
			clientLastName = "Rockks" + Utility.UniqueNumber(4).toLowerCase();
			uniqueClientName = clientFirstName+ " " +clientLastName;
			DCI_Care_Management_CommonMethod.createClientForDisableCareManagement(clientFirstName, clientLastName, uniqueCostCenterName, "", "");
					
			//************ Edit Client ************//
			DCI_Care_Management_CommonMethod.editClientToVerifyEnableCareMgmtNotPresentForDisableCareManagement(uniqueClientName, clientFirstName, clientLastName);
			
			//************ Create an Employee ***************//
			employeeFirstName = "Timm";
			employeeLastName = "Vv" + Utility.UniqueString(4).toLowerCase();
			uniqueemployeeName = employeeFirstName + " " + employeeLastName;	
			DCI_Care_Management_CommonMethod.createEmployeeForDisableCareManagement(employeeFirstName, employeeLastName, uniqueCostCenterName);
			Thread.sleep(1000);		    
			
			//***** Funding Account *****//
	 		DCI_Care_Management_CommonMethod.createfundingAccount(uniqueFundingSourceName, "Hourly", uniqueServiceCodeName, uniqueClientName, uniqueCostCenterName, "Hourly");	 				
			DCI_Care_Management_CommonMethod.disableCareManagementValidationsForOtherUser(uniqueemployeeName.replace(" ", ""),roleType,uniqueemployeeName, uniqueClientName);	
			
			
			DCI_Care_Management_CommonMethod.modifyInstanceLevelSettingsAdminDashboard("Features_CareManagement_IsEnabled", true, "1");
		}
	}


}