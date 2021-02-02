package org.islamcontrib.realmtocsv;

public interface RealmObjectExportable {
    public String getId();

    public String toRow();

    public String getSchema();
}
