 package com.example.Tugas1.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.Tugas1.model.KecamatanModel;
import com.example.Tugas1.model.KeluargaModel;
import com.example.Tugas1.model.KelurahanModel;
import com.example.Tugas1.model.KotaModel;
import com.example.Tugas1.model.PendudukModel;


@Mapper
public interface SipenonDKIMapper {

	@Select("select * from penduduk where nik = #{nik}")
	PendudukModel selectPenduduk(@Param("nik") String nik);
	
	@Select("select * from keluarga where id = #{id}")
	KeluargaModel selectKeluarga(@Param("id") long id);
	
	@Select("select * from kelurahan where id = #{id}")
	KelurahanModel selectKelurahan(@Param("id") long id);
	
	@Select("select * from kecamatan where id= #{id}")
	KecamatanModel selectKecamatan(@Param("id") long id);
	
	@Select("select * from kota where id = #{id}")
	KotaModel selectKota(@Param("id") long id);
	
	@Select("select * from keluarga where nomor_kk = #{nkk}")
	KeluargaModel selectKeluargaNKK(@Param("nkk") String nkk);
	
	@Select("select * from penduduk where id_keluarga = #{id_keluarga}")
	List<PendudukModel> selectPendudukList(@Param("id_keluarga") long id_keluarga);
	
	@Select("select * from kelurahan;")
	List<KelurahanModel> selectAllKelurahan();

	@Select("select * from kecamatan;")
	List<KecamatanModel> selectAllKecamatan();
	
	@Select("select * from kota;")
	List<KotaModel> selectAllKota();
	
	@Insert("INSERT INTO penduduk (nik, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, " 
			+"is_wni, id_keluarga, agama, pekerjaan, status_perkawinan, status_dalam_keluarga, "
			+ "golongan_darah, is_wafat) VALUES "
			+"(#{nik}, #{nama}, #{tempat_lahir}, #{tanggal_lahir}, #{jenis_kelamin}, #{is_wni}, " 
			+"#{id_keluarga}, #{agama}, #{pekerjaan}, #{status_perkawinan}, #{status_dalam_keluarga}, "
			+ "#{golongan_darah}, #{is_wafat})")
	boolean addPenduduk(PendudukModel penduduk);
	
	@Select("select count(*) from penduduk where tanggal_lahir = #{tanggal_lahir}")
	int getJumlahByTanggalLahir(String tanggal_lahir);
	
	
	
	@Select("select count(*) from keluarga where nomor_kk like #{nomor_kk} and id_kelurahan = #{id_kelurahan}")
	int getJumlahKeluarga(@Param("nomor_kk")String nomor_kk, @Param("id_kelurahan")long id_kelurahan);
	
	
	@Insert("INSERT INTO keluarga (nomor_kk, alamat, rt, rw, id_kelurahan, is_tidak_berlaku) VALUES "
			+"(#{nomor_kk}, #{alamat}, #{rt}, #{rw}, #{id_kelurahan}, #{is_tidak_berlaku})")
	boolean addKeluarga(KeluargaModel keluarga);
	
	@Update("UPDATE penduduk " + 
			"SET nik=#{nik}, nama = #{nama}, tempat_lahir = #{tempat_lahir}, tanggal_lahir= #{tanggal_lahir},"+
			" jenis_kelamin = #{jenis_kelamin}, is_wni= #{is_wni}, id_keluarga= #{id_keluarga}, agama = #{agama},"+
			" pekerjaan = #{pekerjaan}, status_perkawinan = #{status_perkawinan}, status_dalam_keluarga = #{status_dalam_keluarga},"+
			" golongan_darah= #{golongan_darah}, is_wafat = #{is_wafat} where id = #{id};")
	boolean editPenduduk(PendudukModel penduduk);
	
	
	@Select("select * from kelurahan where nama_kelurahan = #{nama_kelurahan};")
	KelurahanModel selectKelurahanByName(@Param("nama_kelurahan") String nama);
	
	@Update("UPDATE keluarga " + 
			"SET nomor_kk = #{nomor_kk}, alamat = #{alamat}, rt = #{rt}, rw= #{rw},"+
			" id_kelurahan = #{id_kelurahan} where id = #{id};")
	boolean editKeluarga(KeluargaModel keluarga);
	
	
	
	@Select("select * from penduduk where id_keluarga = #{id_keluarga}")
	PendudukModel selectPendudukDescend(@Param("id_keluarga") String id_keluarga);
	
	@Select("select * from keluarga where id_kelurahan = #{id_kelurahan}")
	KeluargaModel selectKeluargaDescend(@Param("id_kelurahan") long id_kelurahan);
	
	@Select("select * from kelurahan where id_kecamatan = #{id_kecamatan}")
	KelurahanModel selectKelurahanDescend(@Param("id_kecamatan") long id_kecamatan);

	
	@Update("UPDATE penduduk " + 
			"SET is_wafat = 1 where id = #{id};")
	Boolean setMati(@Param("id") long id);

	@Update("UPDATE keluarga " + 
			"SET is_tidak_berlaku = 1 where id = #{id};")
	Boolean setKeluargaInaktif(long id);

	@Select("select * from kecamatan where id_kota = #{id_kota}")
	List<KecamatanModel> selectKecamatanByKota(@Param("id_kota") long id_kota);

	@Select("select * from kelurahan where id_kecamatan = #{id_kecamatan}")
	List<KelurahanModel> selectKelurahanByKecamatan(@Param("id_kecamatan") long id_kecamatan);
	
	
	@Select("select * from penduduk, keluarga, kelurahan where penduduk.id_keluarga = keluarga.id and"
			+ " keluarga.id_kelurahan = kelurahan.id and kelurahan.id = #{id_kelurahan}")
	List<PendudukModel> selectPendudukByKelurahan(@Param("id_kelurahan") long id_kelurahan);
	
	
}