Class otw.iris.excel
{

ClassMethod getHeaders(
	pSheet As %String = 0,
	ByRef pHeaders,
	ByRef pHeadata) As %Status
{
	Set tSC	 = $$$OK
	Set tRow = 0
	Set tCol = ""
	For {
		Set tCol = $Order(^excel(pSheet,tRow,tCol),1,tData)
		If (tCol = "") { Quit }
		Set pHeaders(tCol) = $Get(^excel(pSheet,tRow,tCol))
		If (tData = "") { Continue }
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
	Set colAccount = $Get(pHeadata("Account")) // If (colAccount < 1) Quit tSC
	Set colCategory = $Get(pHeadata("Category")) // If (colCategory < 1) Quit tSC
	Set colCheck = $Get(pHeadata("Check")) // If (colCheck < 1) Quit tSC
	Set colCredit = $Get(pHeadata("Credit"))
	Set colDebit = $Get(pHeadata("Debit")) // If (colDebit < 1) Quit tSC
	If (colCredit < 1) {
		Set colCredit = $Get(pHeadata("Deposit"))
		Set colDebit = $Get(pHeadata("Withdraw"))
	}
	Set colMemo = $Get(pHeadata("Memo")) // If (colMemo < 1) Quit tSC
	Set colMerchant = $Get(pHeadata("Merchant")) // If (colMerchant < 1) Quit tSC
	Set colStatus = $Get(pHeadata("Status")) // If (colStatus < 1) Quit tSC
	Set colSubCategory = $Get(pHeadata("SubCategory")) // If (colSubCategory < 1) Quit tSC
	Set colTranDate = $Get(pHeadata("Date")) // If (colTranDate < 1) Quit tSC
	Set tRow = 0
	For {
		Set tRow = $Order(^excel(pSheet,tRow),1)
		If (tRow = "") { Quit }
		Set o = ##class(dc.iris.transact).%New()
		If colAccount Set o.Account = $Get(^excel(pSheet,tRow,colAccount))
		If colCategory Set o.Category = $Get(^excel(pSheet,tRow,colCategory))
		If colCheck Set o.CheckNumber = $Get(^excel(pSheet,tRow,colCheck))
		If colCredit Set o.Credit = $Get(^excel(pSheet,tRow,colCredit))
		If colDebit Set o.Debit = $Get(^excel(pSheet,tRow,colDebit))
		If colMemo Set o.Memo = $Get(^excel(pSheet,tRow,colMemo))
		If colMerchant Set o.Merchant = $Get(^excel(pSheet,tRow,colMerchant))
		If colStatus Set o.Status = $Get(^excel(pSheet,tRow,colStatus))
		If colSubCategory Set o.SubCategory = $Get(^excel(pSheet,tRow,colSubCategory))
		If colTranDate Set o.TranDate = $Get(^excel(pSheet,tRow,colTranDate))
		Set o.myId = $Increment(^GLOBAL)
		Set tSC = o.%Save()
		If $$$ISERR(tSC) { Quit }
	}
	Quit tSC
}

}
