Class otw.iris.excel
{

ClassMethod getHeaders(pSheet As %String = 0, ByRef pHeaders, ByRef pHeadata) As %Status
{
	Set tSC	 = $$$OK
	Set tRow = 0
	Set tCol = ""
	For {
		Set tCol = $Order(^excel(pSheet,tRow,tCol),1,tData)
		If (tCol = "") { Quit }
		Set pHeaders(tCol) = $Get(^excel(pSheet,tRow,tCol))
		If (tData = "") { Continue }
		If ($Extract(tData,1,4) = "Date") Set tData = "Date"
		Set pHeadata(tData) = tCol
	}
	Quit tSC
}

ClassMethod importExcel(pSkipSheets = 4) As %Status
{
	Set tSC = $$$OK
	Set tSheet = $Get(pSkipSheets)
	For {
		Set tSheet = $Order(^excel(tSheet),1)
		If (tSheet = "") { Quit }
		Set tSC = ..importExcelSheet(tSheet)
		If $$$ISERR(tSC) { Quit }
	}
	Quit tSC
}

ClassMethod importExcelSheet(pSheet As %String = "") As %Status
{
	Set tSC = $$$OK
	If (pSheet = "") { Quit tSC }
	Set tSC = ..getHeaders(pSheet,.pHeaders,.pHeadata)
	Set colAccount = $Get(pHeadata("Account"),-1) // If (colAccount < 1) Quit tSC
	Set colCategory = $Get(pHeadata("Category"),-1) // If (colCategory < 1) Quit tSC
	Set colCheck = $Get(pHeadata("Check"),-1) // If (colCheck < 1) Quit tSC
	Set colCredit = $Get(pHeadata("Credit"),-1)
	Set colDebit = $Get(pHeadata("Debit"),-1) // If (colDebit < 1) Quit tSC
	If (colCredit < 0) {
		Set colCredit = $Get(pHeadata("Deposit"),-1)
		Set colDebit = $Get(pHeadata("Withdraw"),-1)
	}
	Set colMemo = $Get(pHeadata("Memo"),-1) // If (colMemo < 1) Quit tSC
	Set colMerchant = $Get(pHeadata("Merchant"),-1) // If (colMerchant < 1) Quit tSC
	Set colStatus = $Get(pHeadata("Status"),-1) // If (colStatus < 1) Quit tSC
	Set colSubCategory = $Get(pHeadata("SubCategory"),-1) // If (colSubCategory < 1) Quit tSC
	Set colTranDate = $Get(pHeadata("Date"),-1) // If (colTranDate < 1) Quit tSC
	Set tRow = 0
	For {
		Set tRow = $Order(^excel(pSheet,tRow),1)
		If (tRow = "") { Quit }
		Set (tCredit,tDebit) = ""
		Set o = ##class(dc.iris.transact).%New()
		If (colAccount > -1) Set o.Account = $Get(^excel(pSheet,tRow,colAccount))
		If (colCategory > -1) Set o.Category = $Get(^excel(pSheet,tRow,colCategory))
		If (colCheck > -1) Set o.CheckNumber = $Get(^excel(pSheet,tRow,colCheck))
		If (colCredit > -1) Set tCredit = $Get(^excel(pSheet,tRow,colCredit))
		If (colDebit > -1) Set tDebit = $Get(^excel(pSheet,tRow,colDebit))
		If (colMemo > -1) Set o.Memo = $Get(^excel(pSheet,tRow,colMemo))
		If (colMerchant > -1) Set o.Merchant = $Get(^excel(pSheet,tRow,colMerchant))
		If (colStatus > -1) Set o.Status = $Get(^excel(pSheet,tRow,colStatus))
		If (colSubCategory > -1) Set o.SubCategory = $Get(^excel(pSheet,tRow,colSubCategory))
		If (colTranDate > -1) Set o.TranDate = $Get(^excel(pSheet,tRow,colTranDate))
		If ("" = tCredit),("" = tDebit) { CONTINUE }
		Set o.myId = $Increment(^GLOBAL)
		Set o.Credit = tCredit
		Set o.Debit = tDebit
		Set tSC = o.%Save()
		If $$$ISERR(tSC) {
			Do $SYSTEM.Status.DisplayError(tSC)
			QUIT
		}
	}
	Quit tSC
}

}
