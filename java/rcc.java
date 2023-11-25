import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISConnection;
import java.sql.DriverManager;
import java.util.Scanner;

public class rcc {
   
	public static void main(String[] args) {
		System.out.println("\n\tWelcome to IRIS NativeAPI CommandLine Extension\n") ;
	// init connection
		String ip   = cmd("serverIP","127.0.0.1") ;
		String port = cmd("serverPORT","1972") ;
		String nspc = cmd("namespace","USER") ;
		String user = cmd("username","_SYSTEM") ;
		String pwd  = cmd("password","SYS") ;
	// get connected
	 try {
		IRISConnection conn = (IRISConnection) DriverManager.getConnection("jdbc:IRIS://"+ip+":"+port+"/"+nspc,user,pwd);
    IRIS iris = IRIS.createIRIS(conn) ;

    String inst = act(iris, "quit ##class(%SYS.System).GetInstanceName()") ;
		String node=act(iris, "quit ##class(%SYS.System).GetNodeName()") ;
    System.out.println("\nConnected to Instance "+inst+" on Server "+node+"\n") ;
    String demo = "0";
// the main loop   
    while (demo.length()>0) {
      demo = menue(iris);
      }
// game over  
    String ok = cmd("exit","OK") ;
    iris.close();
    conn.close();
    System.out.println("\nThank you for trying the demo\n");
		}
  catch (Exception ex) {
          System.out.println("Exception -- "+ex.getMessage());
    }
    return ;
	}
// ask user	
	public static String cmd( String what, String deflt) {
		Scanner sc = new Scanner(System.in);
		System.out.print(">>> "+what+" ["+deflt+"]: ") ;
		String ans = sc.nextLine() ;
    if (ans.isEmpty()) {
			ans=deflt ;
			}
		return ans ;
	}
	// talk to NativeAPI Extension
	static String act(IRIS iris,String what) {
		String ans=iris.functionString("x","%ZX",what+" quit 0") ;	
		return ans ;
	}
	// demo menue + exercise
	static String menue(IRIS iris) {
		System.out.println("\nSelect Demo to exercise") ;
		System.out.println(" 0 = free ObjectScript") ;
		System.out.println(" 1 = $ZV from Server") ;
		System.out.println(" 2 = Actual Time in Server") ;
		System.out.println(" 3 = TimeZone Offset of Server") ;
		System.out.println(" 4 = Server Architecture*Vendor*Model") ;
		System.out.println(" 5 = List Global in ZWRITE style") ;
		System.out.println(" * = Terminate demo") ;
    Scanner sc = new Scanner(System.in);
    String ans = sc.nextLine() ;
    String res=" ? ? ? " ;
    String skp ="" ;
    switch (ans){
                case "0": 
                        skp = cmd("Your ObjectScript"," quit \"?\" ") ;
                        res = act(iris,skp) ;
                        break;                
                case "1": 
                        res = act(iris,"quit $ZV") ;
                        break;
                case "2": 
                        res = act(iris, "quit $ZDT($h,3)") ;
                        break;
                case "3": 
                        res = act(iris, "quit $ZTZ") ;
                        break;
                case "4": 
                        skp = act(iris,"set cp=$system.CPU.%New()") ;
                        res = act(iris, "quit cp.Arch") ;
                        res = res+" * "+act(iris, "quit cp.Vendor") ; 
                        res = res+" * "+act(iris, "quit cp.Model")  ;
                        skp = act(iris, "kill cp") ;
                        break;
                case "5": 
                        skp = cmd("Your Global","^dc.MultiD") ;
                        gdemo(iris, skp) ;
                        res="**** done ****" ;
                        break;
                case "*": 
                        ans="" ;
                        res="" ;
                        break;
               default: 
                        res="Demo "+ans+" not implemented" ; 
                        ans="??" ;
                        break; 
        }
    System.out.println("\t"+res) ;  
		return ans ;
	}
	// show Global + Content
    static void gdemo(IRIS iris, String glob) {
      int dd = 0;
      if (glob == null) {
        return ;
        }
      dd = Integer.valueOf(act(iris,"q $d("+glob+")")) ;
      if (dd%10 > 0) {
        dzr(iris,glob) ;
         }
      while (dd>9) {
        glob=act(iris, "q $q(@$zr)") ;
        if (glob == null ) {
        //    dd=0 ;
          return ;
          }
        dzr(iris, glob) ;
        }
      return ;
    }

// display global node + value
  static void dzr(IRIS iris,String glob) {
    String res = act(iris, "q ##class(%Utility).FormatString(@$zr)") ;
    System.out.println("\t"+glob+" = "+res) ;
    return ;
    }	
}
