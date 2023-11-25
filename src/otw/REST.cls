Class otw.REST
{

ClassMethod TestApi() As %Status
{
    Set tSC = $$$OK
    set file="/c/cdw.swagger.json"
    set exists = ##class(%File).Exists(file)
    //quit exists
    set obj = ##class(%DynamicAbstractObject).%FromJSONFile(file)
    do ##class(%REST.API).CreateApplication("cdw",.obj,,.new,.error)
    zw new
    zw error
    Quit tSC
}

}
