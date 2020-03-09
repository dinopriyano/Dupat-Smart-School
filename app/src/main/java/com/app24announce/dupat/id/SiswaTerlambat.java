package com.app24announce.dupat.id;

public class SiswaTerlambat {
    private String kelas,keterangan,nama,nis;

    public SiswaTerlambat() {
    }

    public SiswaTerlambat(String kelas, String keterangan, String nama, String nis) {
        this.kelas = kelas;
        this.keterangan = keterangan;
        this.nama = nama;
        this.nis = nis;
    }

    public String getKelas() {
        return kelas;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getNama() {
        return nama;
    }

    public String getNis() {
        return nis;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }
}
