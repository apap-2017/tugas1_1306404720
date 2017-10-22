package com.example.Tugas1.service;

import java.util.List;

import com.example.Tugas1.model.KecamatanModel;
import com.example.Tugas1.model.KeluargaModel;
import com.example.Tugas1.model.KelurahanModel;
import com.example.Tugas1.model.KotaModel;
import com.example.Tugas1.model.PendudukModel;

public interface SipenonDKIService {
	PendudukModel selectPenduduk(String nik);
	KeluargaModel selectKeluarga(long id);
	KelurahanModel selectKelurahan(long id);
	KecamatanModel selectKecamatan(long id);
	KotaModel selectKota(long id);
	
	KeluargaModel selectKeluargaNKK(String nkk);
	List<PendudukModel> selectPendudukList(long id_keluarga);
	
	boolean addPenduduk(PendudukModel penduduk);
	int getJumlahByTanggalLahir(String tanggal_lahir);
	
	boolean addKeluarga(KeluargaModel keluarga);
	List<KelurahanModel> selectAllKelurahan();
	List<KecamatanModel> selectAllKecamatan();
	List<KotaModel> selectAllKota();
	
	boolean editPenduduk(PendudukModel penduduk);
	
	boolean editKeluarga(KeluargaModel keluarga);
	
	
	KelurahanModel selectKelurahanByName(String name);
	boolean setMati(String nikMati);
	
	List<KecamatanModel> selectKecamatanByKota(String kt);
	List<KelurahanModel> selectKelurahanByKecamatan(String kc);
	List<PendudukModel> selectPendudukByKelurahan(String kl);
	

	
}
