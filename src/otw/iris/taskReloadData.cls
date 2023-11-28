Class otw.iris.taskReloadData Extends %SYS.Task.Definition
{

Parameter TaskName = "Reload Data";

/// How many sheets at the beginning of Workbook will be skipped
Property SkipSheets As %Integer(MINVAL = -1) [ InitialExpression = -1 ];

ClassMethod FindMyID() As %String
{
	Set pQuery = "SELECT ID FROM %SYS.Task WHERE TaskClass = 'otw.iris.taskReloadData'"
	Set pResult = ""
	Set rs = ##class(%ResultSet).%New()
	Set tSC = rs.Prepare(pQuery)
	If $$$ISOK(tSC) {
		Set tSC = rs.Execute()
		If $$$ISOK(tSC), rs.Next() Set pResult = rs.GetData(1)
	}
	Do rs.Close()
	Set rs = ""
	If (pResult '= "") { Quit pResult }
	Set objTask = ##class(%SYS.Task).%New()
	Set objTask.Description = "Reload Data from Excel"
	Set objTask.MirrorStatus = 3  // "Any"
	Set objTask.Name = ..#TaskName
	Set objTask.NameSpace = "USER"
	Set objTask.RunAsUser = "_SYSTEM"
	Set objTask.TaskClass = "otw.iris.taskReloadData"
	Set objTask.TimePeriod = 5  // "On Demand"
	Set tSC = objTask.%Save()
	Set pResult = objTask.%Id()
	Quit pResult
}

ClassMethod ReloadData(pSkipSheets As %Integer = -1) As %Status
{
	Kill ^excel
	Set q = """"
	Set tFilename = "oliverzf.txt"
	Set pFlags = "/SHELL/STDOUT="_q_tFilename_q
	Set pCmd = "java IRISNative"
	Set pArg = "" //Set pArg = "-s"
	If (pArg = "") {
		Set tReturn = $ZF(-100,pFlags,pCmd)
	} Else {
		Set tReturn = $ZF(-100,pFlags,pCmd,pArg)
	}
	Write "tReturn = ",tReturn,!
	//Hang 1
	Set tReadFile = ""
	Set timeout = 2
	Try {
		Open tFilename:("R"):timeout  // read
		If $test {
			Use tFilename
			Read tReadFile
			Close tFilename
		}
	} Catch {
	}
	Do ##class(%SYS.System).WriteToConsoleLog("otw.iris.taskReloadData: "_tReadFile,0,0)
	Kill ^GLOBAL
	Set tSC = ##class(otw.iris.excel).importExcel(pSkipSheets)
	Quit tSC
}

Method OnTask() As %Status
{
	Quit ##class(otw.iris.taskReloadData).ReloadData(..SkipSheets)
}

ClassMethod RunNow() As %Status
{
	Set tID = ..FindMyID()
	Set tSC = ##class(%SYS.Task).RunNow(tID)
	Quit tSC
}

}
