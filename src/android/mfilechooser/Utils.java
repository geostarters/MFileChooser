package com.maginsoft.utils;

import java.io.File;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;

import android.util.Log;

import com.maginsoft.data.Category;

public class Utils {

	private static final boolean LOG = false;

	public static int getDepth(File file) {
	      if (file.getParent() == null || new File(file.getParent()).getPath().equals(new File(file.getPath())))
	          return 1;
	      return 1 + getDepth(new File(file.getParent()));
	}

	public static Category getExternalStorage(Context context)
	{

		int external = context.getResources().getIdentifier("external", "string", context.getPackageName());

		File f = new File(getInternalStorage(context).path);
                File mDaddy = new File(f.getParent());
                int count = getDepth(mDaddy);
                while(count > 2){
                    mDaddy = new File(mDaddy.getParent());
                    count = getDepth(mDaddy);
                }

                for (File kid : mDaddy.listFiles()){

                    if(LOG) Log.i("ionic 1", "1.kid.getName(): "+kid.getName());

                    if ((kid.getName().toLowerCase().indexOf("ext") > -1 || kid.getName().toLowerCase()
                            .indexOf("sdcard1") > -1)
                            && !kid.getPath().equals(new File(getInternalStorage(context).path).getPath())
                            && kid.canRead()
                            && kid.canWrite()) {

                    	Category kid2 = new Category();
                    	kid2.path = kid.getAbsolutePath();
                    	kid2.title = context.getString(external);
                        return kid2;
                }else if(isSdCard(kid.getName())) {
                    if(LOG) Log.i("ionic 1", "issdcard true: "+kid.getName());
                    Category kid2 = new Category();
                    kid2.path = kid.getAbsolutePath();
                    kid2.title = context.getString(external);
                    return kid2;
                 }

                /*if (!fallbackToInternal)
                    return null;
                else*/
                   // return getInternalStorage();
                }

                if (new File("/Removable").exists()){
                        for (File kid : new File("/Removable").listFiles()){
                                if (kid.getName().toLowerCase().indexOf("ext") > -1 && kid.canRead()
                                        && !kid.getPath().equals(new File(getInternalStorage(context).path).getPath())
                                        && kid.list().length > 0) {

                                        Category kid2 = new Category();
                                        kid2.path = kid.getAbsolutePath();
                                        kid2.title = context.getString(external);

                                    return kid2;
                                }
                        }

                }

                return null;
	}


                private static boolean isSdCard(String name){
                 Pattern p = Pattern.compile("[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}");
                 Matcher m = p.matcher(name);
                 return m.matches();
                }

	public static Category getExternalStorage2(Context context)
	{

                if(LOG) Log.i("ionic 1", " getExternalStorage");

		int external = context.getResources().getIdentifier("external", "string", context.getPackageName());

		File f = new File(getInternalStorage(context).path);

                if(LOG) Log.i("ionic 1", " Internat oficial: "+ f.getPath());

                File mDaddy = new File(f.getParent());
                int count = getDepth(mDaddy);

                while(count > 2)
                {
                    mDaddy = new File(mDaddy.getParent());
                    count = getDepth(mDaddy);
                }

        for (File kid : mDaddy.listFiles())

            if ((kid.getName().toLowerCase().indexOf("ext") > -1 || kid.getName().toLowerCase().indexOf("sdcard1") > -1)
                    && !kid.getPath().equals(f.getPath())
                    && kid.canRead()
                    && kid.canWrite()){
                        if(LOG) Log.i("ionic 1", "1.kid.getName(): "+kid.getName());
                    	Category kid2 = new Category();
                    	kid2.path = kid.getAbsolutePath();
                    	kid2.title = context.getString(external);
                        return kid2;
                }else{
                        if(LOG) Log.i("ionic 1", "NO troba path external amb SD....... ->"+kid.getPath());
                        if ((kid.getName().toLowerCase().indexOf("remote") == -1 && kid.getName().toLowerCase().indexOf("self") == -1
                                && kid.getPath().toLowerCase().indexOf(f.getPath()) == -1)

                                // && kid.canRead()
                                // && kid.canWrite()
                                ){
                                        if(LOG) Log.i("ionic 1", "3.kid.getName(): "+kid.getPath());
                                        Category kid3 = new Category();
                                        kid3.path = kid.getAbsolutePath();
                                        kid3.title = context.getString(external);
                                        return kid3;
                                }else{
                                        if(LOG) Log.i("ionic 1", "NO troba path no remote");
                                }
                }

        if (new File("/Removable").exists())
            for (File kid : new File("/Removable").listFiles())
                if (kid.getName().toLowerCase().indexOf("ext") > -1 && kid.canRead()
                        && !kid.getPath().equals(new File(getInternalStorage(context).path).getPath())
                        && kid.list().length > 0) {

                        	Category kid2 = new Category();
                        	kid2.path = kid.getAbsolutePath();
                        	kid2.title = context.getString(external);

                                return kid2;
                }
        /*if (!fallbackToInternal)
            return null;
        else*/
           // return getInternalStorage();
        return null;
	}


	public static Category getInternalStorage(Context context){

                int internal = context.getResources().getIdentifier("internal", "string", context.getPackageName());

                File file = Environment.getExternalStorageDirectory();

                if(LOG) Log.i("ionic 1", "Environment.getExternalStorageDirectory(): " + file.getAbsolutePath());

                if(file == null || file.exists() == false){
                        File mnt = new File("/mnt");
                        if (mnt != null && mnt.exists())
                                for (File kid : mnt.listFiles())
                                        if (kid.getName().toLowerCase().indexOf("sd") > -1)
                                        if (kid.canWrite()){
                	                               Category kid1 = new Category();
                                                       kid1.path = kid.getAbsolutePath();
                                                       kid1.title = context.getString(internal);
                                                       return kid1;
                                         }
                }else if (file.getName().endsWith("1")) {
                        File sdcard0 = new File(file.getPath().substring(0, file.getPath().length() - 1)+ "0");
                        if (sdcard0 != null && sdcard0.exists()) file = sdcard0;
                }

                Category kid2 = new Category();
                kid2.path = file.getAbsolutePath();
                kid2.title = context.getString(internal);
                if(LOG) Log.i("ionic 1", "Internal: " + file.getAbsolutePath());
                return kid2;
	}
}
