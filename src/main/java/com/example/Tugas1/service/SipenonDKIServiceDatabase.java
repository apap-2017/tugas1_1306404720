 package com.example.Tugas1.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Tugas1.dao.SipenonDKIMapper;
import com.example.Tugas1.model.KecamatanModel;
import com.example.Tugas1.model.KeluargaModel;
import com.example.Tugas1.model.KelurahanModel;
import com.example.Tugas1.model.KotaModel;
import com.example.Tugas1.model.PendudukModel;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class SipenonDKIServiceDatabase implements SipenonDKIService {
	@Autowired
    private SipenonDKIMapper SipenonDKIMapper;
	
	@Override
	public PendudukModel selectPenduduk(String nik) {
		PendudukModel penduduk = SipenonDKIMapper.selectPenduduk(nik);
		if(penduduk==null) {
			return null;
		}else {
			log.info("NIK "+ nik +"ditemukan");
			penduduk.setKeluarga(selectKeluarga(penduduk.getId_keluarga()));
			return penduduk;
		}
		
	}

	@Override
	public KeluargaModel selectKeluarga(long id) {

		KeluargaModel keluarga = SipenonDKIMapper.selectKeluarga(id);
		if(keluarga == null) {
			return null;
		}else {
			keluarga.setKelurahan(selectKelurahan(keluarga.getId_kelurahan()));
			return keluarga;
		}
	}


	@Override
	public KelurahanModel selectKelurahan(long id) {
		KelurahanModel kelurahan = SipenonDKIMapper.selectKelurahan(id);
		if(kelurahan == null) {
			return null;
		}else {
			kelurahan.setKecamatan(selectKecamatan(kelurahan.getId_kecamatan()));
			return kelurahan;
		}
	}


	@Override
	public KecamatanModel selectKecamatan(long id) {
		KecamatanModel kecamatan = SipenonDKIMapper.selectKecamatan(id);
		if(kecamatan == null) {
			return null;
		}else {
			kecamatan.setKota(selectKota(kecamatan.getId_kota()));
			return kecamatan;
		}
	}


	@Override
	public KotaModel selectKota(long id) {
		KotaModel kota = SipenonDKIMapper.selectKota(id);
		if(kota == null) {
			return null;
		}else {
			return kota;
		}
	}

	@Override
	public KeluargaModel selectKeluargaNKK(String nkk) {
		KeluargaModel keluarga= SipenonDKIMapper.selectKeluargaNKK(nkk);
		if(keluarga == null) {
			return null;
		}else {
			keluarga.setPenduduk(selectPendudukList(keluarga.getId()));
			keluarga.setKelurahan(selectKelurahan(keluarga.getId_kelurahan()));
			return keluarga;
		}
	}
	
	@Override
	public List<PendudukModel> selectPendudukList(long id_keluarga){
		return SipenonDKIMapper.selectPendudukList(id_keluarga);
	}

	@Override
	public boolean addPenduduk(PendudukModel penduduk) {
//		tanggal lahir sifat YYYY-MM-DD
		System.out.println(penduduk);
		KeluargaModel keluarga = selectKeluargaNKK(String.valueOf(penduduk.getId_keluarga()));
		if (keluarga == null) {
			return false;
		}
		String nik = setNewNIK(penduduk, keluarga);
		
		penduduk.setNik(nik);
		penduduk.setStatus_dalam_keluarga(setNewStatusPerkawinan(penduduk));
		System.out.println(setNewStatusPerkawinan(penduduk));
		penduduk.setId_keluarga(keluarga.getId());
		System.out.println(penduduk);
		if(SipenonDKIMapper.addPenduduk(penduduk)) {
			return true;
		} else {
			return false;
		}
//		return true;
	}

	public String setNewNIK(PendudukModel penduduk, KeluargaModel keluarga) {
		System.out.println(penduduk);
		String nik = keluarga.getKelurahan().getKecamatan().getKode_kecamatan().substring(0,6);
		String[] editTanggalLahir = penduduk.getTanggal_lahir().split("-");
		String tanggalLahirNIK="";
		String newTanggalLahir="";
		
		if (penduduk.getJenis_kelamin() == 0) {
			System.out.println("pria");
			for(int i = editTanggalLahir.length-1; i >= 0; i--) {
				System.out.println(editTanggalLahir[i]);
				
				if(i==0) {
					tanggalLahirNIK = tanggalLahirNIK + editTanggalLahir[i].substring(2);
				}else {
					tanggalLahirNIK = tanggalLahirNIK + editTanggalLahir[i];
				}
				if (i==0) {
					newTanggalLahir = newTanggalLahir + editTanggalLahir[i];
				}else {
					newTanggalLahir = newTanggalLahir + editTanggalLahir[i] + "-";
				}
			}
			System.out.println(tanggalLahirNIK);
			nik = nik + tanggalLahirNIK + "0001";
			long a = Long.parseLong(nik);
			
			
			a = a + getJumlahByTanggalLahir(penduduk.getTanggal_lahir());
			nik = String.valueOf(a);
			System.out.println(nik);
			return nik;
		}else {
			System.out.println("wanita");
			System.out.println(penduduk.getTanggal_lahir());
			for(int i = editTanggalLahir.length-1; i >= 0; i--) {
				System.out.println(editTanggalLahir[i]);
				if(i == editTanggalLahir.length-1) {
					int a = Integer.parseInt(editTanggalLahir[i]);
					a = a + 40;
					tanggalLahirNIK = tanggalLahirNIK+String.valueOf(a);
				}else {
					if(i==0) {
						tanggalLahirNIK = tanggalLahirNIK + editTanggalLahir[i].substring(2);
					}else {
						tanggalLahirNIK = tanggalLahirNIK + editTanggalLahir[i];
					}
				}					
				if (i==0) {
					
					newTanggalLahir = newTanggalLahir + editTanggalLahir[i];
				}else {
					newTanggalLahir = newTanggalLahir + editTanggalLahir[i] + "-";
				}
			}
			System.out.println(newTanggalLahir+ " " + tanggalLahirNIK);
			nik = nik + tanggalLahirNIK + "0001";
			
			long a = Long.parseLong(nik);
			
			a = a + getJumlahByTanggalLahir(penduduk.getTanggal_lahir());
			nik = String.valueOf(a);
			System.out.println(nik);
			return nik;	
			
		}
	}
	
	public String setNewStatusPerkawinan(PendudukModel penduduk) {
		switch (penduduk.getStatus_perkawinan()) {
		case "Kawin":
			switch(penduduk.getJenis_kelamin()) {
			case 0:
				return "Kepala Keluarga";
			case 1:
				return "Istri";
			}
		case "Belum Kawin":
			return "Anak";
		case "Cerai Mati":
			return "Famili Lain";
		case "Cerai Hidup":
			return "Famili Lain";
		}
		return null;
	}
	
	
	@Override
	public int getJumlahByTanggalLahir(String tanggal_lahir) {
		return SipenonDKIMapper.getJumlahByTanggalLahir(tanggal_lahir);
	}

	@Override
	public boolean addKeluarga(KeluargaModel keluarga) {
		KelurahanModel kelurahan = SipenonDKIMapper.selectKelurahan(keluarga.getId_kelurahan());
		String toNKK = kelurahan.getKode_kelurahan().substring(0,6);
		LocalDateTime sekarang = LocalDateTime.now();
		String hari = String.valueOf(sekarang.getDayOfMonth());
		String todayDate = hari + "-" + String.valueOf(sekarang.getMonthValue()) + "-" +String.valueOf(sekarang.getYear());
		System.out.print(todayDate);
		System.out.print("ini");
		if(hari.length() == 1) {
			hari = "0" + hari;
		}
		String NKKsearch = hari + String.valueOf(sekarang.getMonthValue()) + String.valueOf(sekarang.getYear()).substring(2,4); 
		toNKK = toNKK + NKKsearch;
		toNKK = toNKK + "0001";
		long NKKcount = Long.parseLong(toNKK);
		int plus = SipenonDKIMapper.getJumlahKeluarga("%"+NKKsearch+"%", keluarga.getId_kelurahan());
		System.out.println(NKKsearch + " " + plus + keluarga.getId_kelurahan());
		NKKcount = NKKcount + plus;
		toNKK = String.valueOf(NKKcount);
		
		keluarga.setNomor_kk(toNKK);
		System.out.println(keluarga);
		if(SipenonDKIMapper.addKeluarga(keluarga)) {
			return true;
		}else return false;
	}

	@Override
	public List<KelurahanModel> selectAllKelurahan() {
		
		return SipenonDKIMapper.selectAllKelurahan();
	}

	@Override
	public List<KecamatanModel> selectAllKecamatan() {
		
		return SipenonDKIMapper.selectAllKecamatan();
	}

	@Override
	public List<KotaModel> selectAllKota() {
		
		return SipenonDKIMapper.selectAllKota();
	}

	@Override
	public boolean editPenduduk(PendudukModel penduduk) {
		PendudukModel update = SipenonDKIMapper.selectPenduduk(penduduk.getNik());
		System.out.println("this "+ penduduk);
		System.out.println("update "+ update);
//		set seluruh yang bisa diset.
		update.setNama(penduduk.getNama());
		update.setTempat_lahir(penduduk.getTempat_lahir());
		update.setTanggal_lahir(penduduk.getTanggal_lahir());
		update.setJenis_kelamin(penduduk.getJenis_kelamin());
		update.setGolongan_darah(penduduk.getGolongan_darah());
		update.setAgama(penduduk.getAgama());
		update.setStatus_perkawinan(penduduk.getStatus_perkawinan());
		update.setPekerjaan(penduduk.getPekerjaan());
		update.setIs_wni(penduduk.getIs_wni());
		update.setIs_wafat(penduduk.getIs_wafat());
		update.setId_keluarga(penduduk.getId_keluarga());
//		pakai method
		KeluargaModel keluarga = selectKeluarga(penduduk.getId_keluarga());
		update.setStatus_dalam_keluarga(setNewStatusPerkawinan(update));
		update.setNik(setNewNIK(update, keluarga));
		System.out.println("tosave "+ update);
		SipenonDKIMapper.editPenduduk(update);
		return true;
	}

	@Override
	public KelurahanModel selectKelurahanByName(String name) {
		return SipenonDKIMapper.selectKelurahanByName(name);
	}

	@Override
	public boolean editKeluarga(KeluargaModel keluarga) {
		KeluargaModel update = SipenonDKIMapper.selectKeluargaNKK(keluarga.getNomor_kk());
		System.out.println("this "+ keluarga);
		System.out.println("update "+ update);
		
		keluarga.setId(update.getId());
		
//		set No. KK
		String toNKK = keluarga.getKelurahan().getKode_kelurahan().substring(0,6);
		LocalDateTime sekarang = LocalDateTime.now();
		String hari = String.valueOf(sekarang.getDayOfMonth());
		String todayDate = hari + "-" + String.valueOf(sekarang.getMonthValue()) + "-" +String.valueOf(sekarang.getYear());
		System.out.println(todayDate);
		System.out.println("ini");
		if(hari.length() == 1) {
			hari = "0" + hari;
		}
		String NKKsearch = hari + String.valueOf(sekarang.getMonthValue()) + String.valueOf(sekarang.getYear()).substring(2,4); 
		String thissearch = toNKK + NKKsearch;
		
		toNKK = thissearch + String.valueOf(update.getNomor_kk()).substring(12, 15);
		long NKKcount = Long.parseLong(toNKK);
		System.out.println(thissearch);
		int plus = SipenonDKIMapper.getJumlahKeluarga(thissearch+"%", keluarga.getId_kelurahan());
		System.out.println(plus + " "+ keluarga.getId_kelurahan());
		if (plus>=1) {
			NKKcount = NKKcount + plus;
		}
		toNKK = String.valueOf(NKKcount);
		System.out.println(toNKK);
		keluarga.setNomor_kk(toNKK);
		System.out.println(keluarga);
		SipenonDKIMapper.editKeluarga(keluarga);
		return true;
		
	}


	@Override
	public boolean setMati(String nikMati) {
		PendudukModel setMati = selectPenduduk(nikMati);
		
		SipenonDKIMapper.setMati(setMati.getId());
		
		KeluargaModel cekKeluargaInaktif = selectKeluargaNKK(setMati.getKeluarga().getNomor_kk());
		List<PendudukModel> cekaktif = cekKeluargaInaktif.getPenduduk();
		int all = cekaktif.size();
		for (PendudukModel penduduk : cekaktif) {
			if(penduduk.getIs_wafat()=="1") {
				all = all - 1;
			}
		}
		if(all == 0) {
			SipenonDKIMapper.setKeluargaInaktif(cekKeluargaInaktif.getId());
		}
		return true;
	}

	

	@Override
	public List<KecamatanModel> selectKecamatanByKota(String kt) {
		return SipenonDKIMapper.selectKecamatanByKota(Long.parseLong(kt));
	}

	@Override
	public List<KelurahanModel> selectKelurahanByKecamatan(String kc) {
		return SipenonDKIMapper.selectKelurahanByKecamatan(Long.parseLong(kc));
	}

	@Override
	public List<PendudukModel> selectPendudukByKelurahan(String kl) {
		return SipenonDKIMapper.selectPendudukByKelurahan(Long.parseLong(kl));
	}

	
	
	
}
