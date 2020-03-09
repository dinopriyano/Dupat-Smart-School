package com.app24announce.dupat.id;

public class SetGetInsideDetailJadwal {

    String mapel,timestamp,id,hari,waktuawal,waktuakhir;

    public SetGetInsideDetailJadwal() {
    }

    public SetGetInsideDetailJadwal(String mapel, String timestamp, String id, String hari, String waktuawal, String waktuakhir) {
        this.mapel = mapel;
        this.timestamp = timestamp;
        this.id = id;
        this.hari = hari;
        this.waktuawal = waktuawal;
        this.waktuakhir = waktuakhir;
    }


    public String getMapel() {
        return mapel;
    }

    public void setMapel(String mapel) {
        this.mapel = mapel;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getWaktuawal() {
        return waktuawal;
    }

    public void setWaktuawal(String waktuawal) {
        this.waktuawal = waktuawal;
    }

    public String getWaktuakhir() {
        return waktuakhir;
    }

    public void setWaktuakhir(String waktuakhir) {
        this.waktuakhir = waktuakhir;
    }
}
