package GUI;

import java.io.*;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

class Methods
{
	public static double eccentricitysquare(double flattening)
	{
		return flattening*(2-flattening);
	}
        
        
	public static double sincosfunc(double value,int val)
	{
		//longitude and latitude ko pehle radian me calculate then sine ya cos lagega
		if(val==1)
		{
			return(Math.sin(Math.toRadians(value)));
		}
		else
		{
			return(Math.cos(Math.toRadians(value)));
		}
	}
        
        
	public static double dellatitude(double delx,double dely,double delz,double sinlat,double sinlongg,double coslat,double coslongg,double height,double flattening,double delflattening,double delmajor,double major)
	{
		//method that return delta of latitude
		double esquare,rad,prad;
		//eccentricity ka square 
		esquare=eccentricitysquare(flattening);
		// square of sin of lat
		double sqSinLat=Math.pow(sinlat,2);
		//radius of curvature of the ellipsoid in prime vertical plane
		rad=major/Math.sqrt((1-esquare*sqSinLat));
		
		//radius of curvature of the ellipsoid in meridian plane
		prad=major*(1-esquare)/Math.pow((1-esquare*sqSinLat),1.5);
		return(((-delx*sinlat*coslongg-dely*sinlat*sinlongg+delz*coslat+((rad*esquare*sinlat*coslat*delmajor)/major)+sinlat*coslat*((prad/(1-flattening))+rad*(1-flattening))*delflattening)/(prad+height)));
		
	}
        
        
	public static double dellongitude(double delx,double dely,double sinlat,double sinlongg,double coslat,double coslongg,double height,double major,double flattening)
	{
		//method that return delta of latitude
		double esquare,rad;
		//eccentricity ka square 
		esquare=eccentricitysquare(flattening);
		// square of sin of lat
		double sqSinLat=Math.pow(sinlat,2);
		//radius of curvature of the ellipsoid in prime vertical plane
		rad=major/Math.sqrt((1-esquare*sqSinLat));
		return((-delx*sinlongg+dely*coslongg)/((rad+height)*coslat));
	}
        
        
	public static double del_height(double delx,double dely,double delz,double sinlat,double sinlongg,double coslat,double coslongg,double major,double flattening,double delflattening,double delmajor)
	{
		//method that return delta of latitude
		double esquare,rad;
		//eccentricity ka square 
		esquare=eccentricitysquare(flattening);
		// square of sin of lat
		double sqSinLat=Math.pow(sinlat,2);
		//radius of curvature of the ellipsoid in prime vertical plane
		rad=major/Math.sqrt((1-esquare*sqSinLat));
		return(delx*coslat*coslongg+dely*coslat*sinlongg+delz*sinlat-((major*delmajor)/rad)+rad*(1-flattening)*sqSinLat*delflattening);
	}
        
   
	public double[] convertor(double plat, double plongg, double pheight, double pmajor, double pminor, double pflat, double pmajor1, double pminor1, double pflat1, double pdelx, double pdely, double pdelz) throws IOException
        {
                 String datum="",text="",rdatum="",cdatum="";
		 double major=0,minor=0,flattening=0,delx=0,dely=0,delz=0,temp;
		 double major1=0,minor1=0,flattening1=0;
                 
                 double ret[]=new double[3];
		 
                 major=pmajor;
		 minor=pminor;
		 temp=pflat;
		 flattening=1/temp;
				
		 major1=pmajor1;
		 minor1=pminor1;
		 temp=pflat1;
		 flattening1=1/temp;
				
		
		//claculating change in major and flattening
		double delmajor=major1-major;
		double delflattening=flattening1-flattening;	
		
		delx=pdelx;
		dely=pdely;
		delz=pdelz;
				
		//Current DATUM KA LATITUDE, LONGITIDE AUR height le rha
		double lat,longg,height;
		lat=plat;
		longg=plongg;
		height=pheight;
		
		//calculating some intermediate values.
		double coslat,sinlat,sinlongg,coslongg;
		//SEND 1 for SIN and 2 for COS..
		coslat=sincosfunc(lat,2);
		sinlat=sincosfunc(lat,1);
		coslongg=sincosfunc(longg,2);
		sinlongg=sincosfunc(longg,1);
		
		//now calculating delta lat,delta longg, delta height
		double dellat,dellongg,delheight;
		
		//del of latitude
		dellat=dellatitude(delx,dely,delz,sinlat,sinlongg,coslat,coslongg,height,flattening,delflattening,delmajor,major);
		System.out.println("\nDELLAT IN RADIANS: "+dellat);
		System.out.println("\nDELLAT in DEGREES SECOND : "+Math.toDegrees(dellat)*60*60);
		
		//del of longitude
		dellongg=dellongitude(delx,dely,sinlat,sinlongg,coslat,coslongg,height,major,flattening);
		System.out.println("\nDELLONGG in RADIANS: "+dellongg);
		System.out.println("\nDELLONGG in DEGREES SECONDS: "+Math.toDegrees(dellongg)*60*60);
		
		//del of height
		delheight=del_height(delx,dely,delz,sinlat,sinlongg,coslat,coslongg,major,flattening,delflattening,delmajor);
		System.out.println("\nDELHEIGHT : "+delheight);
		
		//printing current datum, its region along with latitude,longitude and height
		System.out.println(datum+" "+rdatum+" "+" "+lat+" "+longg+" "+height);
		
		double nlat=lat+dellat;
		double nlongg=longg+dellongg;
		double nheight=height+delheight;
                DecimalFormat df = new DecimalFormat("#.######");
                ret[0]=Double.parseDouble(df.format(nlat));
                ret[1]=Double.parseDouble(df.format(nlongg));
                ret[2]=Double.parseDouble(df.format(nheight));
		
		//printing new datum its region along with latitude,longitude and height
		System.out.println(cdatum+" "+rdatum+" "+" "+nlat+" "+nlongg+" "+nheight);
                
                return ret;
		
        }
}

class Transformation4
{
    public static double[] calledFromGUI(double lat, double longg, double height, String id1, String id2) throws IOException
    {
        double ret[]=new double[3];
        
        
        System.out.println("Id1="+id1+" Id2="+id2);
        
        double major1=0, minor1=0, flat1=0, major2=0, minor2=0, flat2=0, majorw=0, minorw=0, flatw=0;
        double delx=0, dely=0,delz=0;
        
        String[] id1split = id1.split(" ");
        String[] id2split = id2.split(" ");
        
        //System.out.println("\nnmn"+id1split[0]+id1split[1]+id2split[0]+id2split[1]);
        
        InputStream f1 = Transformation4.class.getResourceAsStream("basic_parameter.csv");
        InputStream f2 = Transformation4.class.getResourceAsStream("to_wgs.csv");
        
        BufferedReader br1 = new BufferedReader(new InputStreamReader(f1));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(f2));
        
        String strLine;
                
        //To Read Basic Parameters from f1
        while ((strLine = br1.readLine()) != null)         //taking parameters from file   
            {
                String[] tokens = strLine.split(",");
                    
                if(tokens[0].equals(id1split[0]))
                {
                    major1 = Double.parseDouble(tokens[1]);
                    minor1 = Double.parseDouble(tokens[2]);
                    flat1  =  Double.parseDouble(tokens[3]);
                }
                
                if(tokens[0].equals(id2split[0]))
                {
                    major2 = Double.parseDouble(tokens[1]);
                    minor2 =  Double.parseDouble(tokens[2]);
                    flat2 = Double.parseDouble(tokens[3]);
                }
                   
                if("WGS84".equals(tokens[0]))
                {
                    majorw = Double.parseDouble(tokens[1]);
                    minorw =  Double.parseDouble(tokens[2]);
                    flatw = Double.parseDouble(tokens[3]);
                }
            }
        
        Methods m=new Methods();
       
        boolean c=true; //condition to check whether identies are not equal and also to check if array out of bound 
        
        //To perform Conversion
        
        //If one of converstion is WGS
        if(!("WGS84".equals(id1split[0]) && "WGS84".equals(id2split[0]))){
        if("WGS84".equals(id1split[0]) || "WGS84".equals(id2split[0]))
        {
            //If from is WGS
            String strLine1;
            String strLine2;
            if("WGS84".equals(id1split[0]))
            {
                while ((strLine1 = br2.readLine()) != null)
                {
                    String[] tokens = strLine1.split(",");
                    if(tokens[0].equals(id2split[1]))
                    {
                        delx=-1*Double.parseDouble(tokens[1]);
                        dely=-1*Double.parseDouble(tokens[2]);
                        delz=-1*Double.parseDouble(tokens[3]);
                    }
                }
                ret=m.convertor(lat,longg,height,major1,minor1,flat1,major2,minor2,flat2,delx,dely,delz);
                //return ret;
            }
            
            //If to is WGS
            if("WGS84".equals(id2split[0]))
            {
                while ((strLine2 = br2.readLine()) != null)
                {
                    String[] tokens = strLine2.split(",");
                    if(tokens[0].equals(id1split[1]))
                    {
                        delx=Double.parseDouble(tokens[1]);
                        dely=Double.parseDouble(tokens[2]);
                        delz=Double.parseDouble(tokens[3]);
                    }
                }
                ret=m.convertor(lat,longg,height,major1,minor1,flat1,major2,minor2,flat2,delx,dely,delz);
                //return ret;
            }
        }
      
        else if(!(id1split[1].equals(id2split[1]))){
        {
            String strLine1;
            String strLine2;
            
            //To WGS
            while ((strLine1 = br2.readLine()) != null)
            {
                String[] tokens = strLine1.split(",");
                if(tokens[0].equals(id1split[1]))
                {
                    delx=Double.parseDouble(tokens[1]);
                    dely=Double.parseDouble(tokens[2]);
                    delz=Double.parseDouble(tokens[3]);
                }
            }
            
            double intermediate[]=new double[3];
            System.out.println("\nIntermediate");
            intermediate=m.convertor(lat,longg,height,major1,minor1,flat1,majorw,minorw,flatw,delx,dely,delz);
            //This will convert to WGS84 now we have convert from WGS84 to that local datum
            //From WGS
            while ((strLine2 = br2.readLine()) != null)
            {
                String[] tokens = strLine2.split(",");
                if(tokens[0].equals(id2split[1]))
                {
                    delx=-1*Double.parseDouble(tokens[1]);
                    dely=-1*Double.parseDouble(tokens[2]);
                    delz=-1*Double.parseDouble(tokens[3]);
                }
            }
            ret=m.convertor(intermediate[0],intermediate[1],intermediate[2],majorw,minorw,flatw,major2,minor2,flat2,delx,dely,delz);
            //return ret;
        }
        }
        else{
            JOptionPane.showMessageDialog(null,"Both Source and Destination Datum is Same!","Error",JOptionPane.INFORMATION_MESSAGE);
        }
        }
        else{
            JOptionPane.showMessageDialog(null,"Both Source and Destination Datum is Same!","Error",JOptionPane.INFORMATION_MESSAGE); 
        }   
     return ret;//End of callFromGUI
    }
    
    public static Double toDecimal(String InputType){    //convert mm  to decimal
        Double value;
        String [] SplitData=InputType.split(" ");
                value=Double.parseDouble(SplitData[0]);
                if(value<0){
                value=value-Double.parseDouble(SplitData[1])/60;
                }else{
                value=value+Double.parseDouble(SplitData[1])/60;    
                }
                return value;
    }
    public static Double toDecimalS(String InputType){   //convert mm ss to decimal
        Double value;
        String [] SplitData=InputType.split(" ");
                value=Double.parseDouble(SplitData[0]);
                if(value<0){
                value=value-Double.parseDouble(SplitData[1])/60-Double.parseDouble(SplitData[2])/3600;
                }else{
                value=value+Double.parseDouble(SplitData[1])/60+Double.parseDouble(SplitData[2])/3600;    
                }
                return value;
    }    
}//End of Class