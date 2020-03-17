package testCases;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import Utilities.DriverManager;
import Utilities.LeerXmls1;
import Utilities.OpenHtml;
import Utilities.Step;
import Utilities.reportMethod;

public class TC1 {

	public static void main(String[] args) throws Exception {
		//Crea objeto tipo WebElementCreator
		DriverManager driverManager=new DriverManager();
		//Crea una lista para guardar los pasos
		ArrayList<ArrayList> tcList;
		ArrayList<Step> stepList;
		//Inicializa el WebDriver
		WebDriver driver;
		System.setProperty("webdriver.chrome.driver","C:\\Users\\Training\\Desktop\\Prueba\\frameWorkBatch3\\driver\\chromedriver.exe" );
		 driver = new ChromeDriver();
		 driverManager.setDriver(driver);
		 //Crea un Objeto OpneHtml
		 OpenHtml openHtml=new OpenHtml();
		//Manda el driver a element
		LeerXmls1 TC = new LeerXmls1();		
		tcList=TC.obtenObjetos("C:\\Users\\Training\\Desktop\\Prueba\\frameWorkBatch3\\excel\\TC.xlsx");
		//lee cada uno de los steps
		for(int x=0;x<tcList.size();x++) {
			//Receive la lista de pasos del test case
			stepList=tcList.get(x);
			//itera cada paso para mandarlos al driver manager
			for(int i=1;i<stepList.size();i++) {
				//imprime el paso 
			System.out.println(stepList.get(i).toString());
			Thread.sleep(1000);
			//se crea un Web element y se ejecutan
			driverManager.executeStep(stepList.get(i));
			}
			reportMethod report= new reportMethod();
			report.reportMaker(tcList.get(x));
		}
		//abre el reporte
		openHtml.OpenHtml();
	}


}
