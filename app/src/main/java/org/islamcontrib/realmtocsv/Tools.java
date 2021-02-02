package org.islamcontrib.realmtocsv;

import io.realm.RealmList;

public class Tools {

    public static String parseRealmList(RealmList list){
        String str="";

        for (int i = 0; i < list.size(); i++) {
            RealmObjectExportable realmObjectExportable = (RealmObjectExportable) list.get(i);
            if(i>0) str+="!###!";
            str += realmObjectExportable;
        }

        return str;
    }

}
