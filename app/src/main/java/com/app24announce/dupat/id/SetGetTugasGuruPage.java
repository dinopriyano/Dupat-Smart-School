package com.app24announce.dupat.id;

public class SetGetTugasGuruPage {
    String id,kelastujuan,keterangan,mapel,namaguru,uid,tanggal,adalampiran;

    public SetGetTugasGuruPage() {
    }

    public SetGetTugasGuruPage(String id, String kelastujuan, String keterangan, String mapel, String namaguru, String uid, String tanggal, String adalampiran) {
        this.id = id;
        this.kelastujuan = kelastujuan;
        this.keterangan = keterangan;
        this.mapel = mapel;
        this.namaguru = namaguru;
        this.uid = uid;
        this.tanggal = tanggal;
        this.adalampiran = adalampiran;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKelastujuan() {
        return kelastujuan;
    }

    public void setKelastujuan(String kelastujuan) {
        this.kelastujuan = kelastujuan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getMapel() {
        return mapel;
    }

    public void setMapel(String mapel) {
        this.mapel = mapel;
    }

    public String getNamaguru() {
        return namaguru;
    }

    public void setNamaguru(String namaguru) {
        this.namaguru = namaguru;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getAdalampiran() {
        return adalampiran;
    }

    public void setAdalampiran(String adalampiran) {
        this.adalampiran = adalampiran;
    }
}
