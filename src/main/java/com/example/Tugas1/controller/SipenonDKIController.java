package com.example.Tugas1.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Tugas1.model.KecamatanModel;
import com.example.Tugas1.model.KeluargaModel;
import com.example.Tugas1.model.KelurahanModel;
import com.example.Tugas1.model.KotaModel;
import com.example.Tugas1.model.PendudukModel;
import com.example.Tugas1.service.SipenonDKIService;


@Controller
public class SipenonDKIController {
	
	@Autowired
    SipenonDKIService SipenonDAO;
	
	@RequestMapping("/")
	public String home(Model model) {
		return "penduduk";
	}
	
	@RequestMapping("/penduduk")
	public String searchPenduduk(@RequestParam(name="nik", required = false) String nik, @ModelAttribute("success") String success, Model model) {
		if(nik == null) {
			return home(model);
		} else {
			PendudukModel search = SipenonDAO.selectPenduduk(nik);
			if(search == null) {
				model.addAttribute("messageNIK", "Penduduk dengan NIK " + nik + " tidak ditemukan!");
				return home(model);
			}else {
				if (!success.isEmpty()) {
					model.addAttribute("message", "Penduduk dengan NIK " + success + " telah diset statusnya sebagai Meninggal.");
				}
				
				model.addAttribute("penduduk", search);
				return "pendudukdetail";
			}
		}
	}
	
	@RequestMapping(value = "/penduduk/mati", method=RequestMethod.POST)
	public String setMati(HttpServletRequest request, Model model, final RedirectAttributes redirectAttributes) {
		
		String nikMati = request.getParameter("action");
		model.addAttribute("message", "Status kematian penduduk dengan NIK " + nikMati + " telah diset sebagai meninggal.");
		SipenonDAO.setMati(nikMati);
		redirectAttributes.addAttribute("success", nikMati);
		return "redirect:/penduduk?nik=" + nikMati;
	}

	@RequestMapping("/keluarga")
	public String searchKeluarga(Model model, @RequestParam(name= "nkk", required = false) String nkk) {
		if(nkk == null) {
			return home(model);
		} else {
			KeluargaModel search = SipenonDAO.selectKeluargaNKK(nkk);
			if(search == null) {
				model.addAttribute("messageNIK", "Penduduk dengan NKK " + nkk + " tidak ditemukan!");
				return home(model);
			}else {
				System.out.println(search.getPenduduk());
				model.addAttribute("keluarga", search);
				return "keluargadetail";
			}
		}
	}

	@RequestMapping("/penduduk/tambah")
	public String tambahPenduduk(Model model, @ModelAttribute PendudukModel penduduk, 
    		HttpServletRequest request) {
		if(request.getMethod().equals("GET")) {
			model.addAttribute("penduduk",new PendudukModel());
			return "tambahpenduduk";
		}else {
			PendudukModel insert = penduduk;
			
			if(SipenonDAO.addPenduduk(insert)) {
				model.addAttribute("messagesuccess", "Sukses! Data Penduduk dengan NIK "+ penduduk.getNik()+" berhasil ditambahkan.");
				model.addAttribute("penduduk",new PendudukModel());
				return "tambahpenduduk";
			}else {
				model.addAttribute("messagesuccess", "Penambahan penduduk gagal, silahkan cek data anda kembali.");
				model.addAttribute("penduduk",new PendudukModel());
				return "tambahpenduduk";
			}
			
		}
	}
	
	@RequestMapping("/keluarga/tambah")
	public String tambahKeluarga(Model model, @ModelAttribute KeluargaModel keluarga, 
			HttpServletRequest request) {
		if(request.getMethod().equals("GET")) {
			model.addAttribute("keluarga",new KeluargaModel());
			model.addAttribute("daftarkecamatan", SipenonDAO.selectAllKecamatan());
			model.addAttribute("daftarkelurahan", SipenonDAO.selectAllKelurahan());
			model.addAttribute("daftarkota", SipenonDAO.selectAllKota());
			return "tambahkeluarga";
		}else {
			KeluargaModel insert = keluarga;
			insert.setIs_tidak_berlaku(0);
			
			KelurahanModel cekLurah = SipenonDAO.selectKelurahan(keluarga.getId_kelurahan());
			KecamatanModel cekCamat = SipenonDAO.selectKecamatan(cekLurah.getId());
			String idcamat = request.getParameter("kecamatan");
			String idkota = request.getParameter("kota");
			System.out.println(!idcamat.equals(String.valueOf(cekLurah.getId_kecamatan())));
			if(idcamat.equals(String.valueOf(cekLurah.getId_kecamatan()))== false) {
				model.addAttribute("message", "Kecamatan tidak cocok dengan kelurahan. Silahkan periksa data anda dan masukkan kembali.");
				model.addAttribute("keluarga",new KeluargaModel());
				model.addAttribute("daftarkecamatan", SipenonDAO.selectAllKecamatan());
				model.addAttribute("daftarkelurahan", SipenonDAO.selectAllKelurahan());
				model.addAttribute("daftarkota", SipenonDAO.selectAllKota());
				return "tambahkeluarga";
			}
			if(!idkota.equals(String.valueOf(cekCamat.getId_kota()))== false) {
				model.addAttribute("message", "Kota tidak cocok dengan kecamatan. Silahkan periksa data anda dan masukkan kembali.");
				model.addAttribute("message", "Kecamatan tidak cocok dengan kelurahan. Silahkan periksa data anda dan masukkan kembali.");
				model.addAttribute("keluarga",new KeluargaModel());
				model.addAttribute("daftarkecamatan", SipenonDAO.selectAllKecamatan());
				model.addAttribute("daftarkelurahan", SipenonDAO.selectAllKelurahan());
				model.addAttribute("daftarkota", SipenonDAO.selectAllKota());
				return "tambahkeluarga";
			}
//			if() {}
//			if() {}
			SipenonDAO.addKeluarga(insert);
			model.addAttribute("message", "Sukses! Data Keluarga dengan NKK "+ keluarga.getNomor_kk()+ " berhasil ditambahkan.");
			model.addAttribute("keluarga",new KeluargaModel());
			return "tambahkeluarga";
		}
	}

	
	@RequestMapping("penduduk/ubah")
	public String cariUbahPenduduk(Model model, @ModelAttribute("success") String success, @PathVariable Optional<String> nik, PendudukModel penduduk,
			HttpServletRequest request) {
		if(request.getMethod().equals("GET")) {
			if(!success.isEmpty()) {
				model.addAttribute("message", "Data dengan NIK " + success + " berhasil diubah.");
			}
			return "cariubahpenduduk";
		}else{
			return "redirect:/penduduk/ubah/"+ request.getParameter("nik");
		}
	}
	
	@RequestMapping("/penduduk/ubah/{nik}")
	public String ubahPenduduk(Model model,  @PathVariable Optional<String> nik, PendudukModel penduduk,
			HttpServletRequest request, final RedirectAttributes redirectAttributes, @ModelAttribute("fail") String fail) {
		
		if(!nik.isPresent()) {
			return "redirect:/penduduk/ubah/";
		}else {
			if(request.getMethod().equals("POST")) {
				System.out.println(request.getMethod());
				System.out.println(penduduk);
				
				
				KeluargaModel keluarga = SipenonDAO.selectKeluargaNKK(request.getParameter("id_keluarga"));
				if(keluarga == null) {
					System.out.println("yakdisisn");
					redirectAttributes.addAttribute("fail",  request.getParameter("id_keluarga"));
					return "redirect:/penduduk/ubah/"+ penduduk.getNik();
				}else {
					System.out.println("masuksini");
					penduduk.setId_keluarga(keluarga.getId());
					System.out.println(penduduk);
					SipenonDAO.editPenduduk(penduduk);
					redirectAttributes.addAttribute("success", nik.get());
					System.out.println(redirectAttributes);
					return "redirect:/penduduk/ubah";
				}
				
			}else {
				
				PendudukModel change = SipenonDAO.selectPenduduk(nik.get());
				System.out.println(change);
				if(change == null) {
					model.addAttribute("message", "Penduduk dengan NIK " + nik + " tidak ditemukan.");
					return "redirect:/penduduk/ubah";
				} else {
					model.addAttribute("penduduk", change);
					if(!fail.isEmpty()) {
						model.addAttribute("message", "Nomor KK dengan nomor " + fail + " Tidak ditemukan. Silahkan periksa data kembali.");
					}
					return "ubahpenduduk";
				}
			}
		}
	}
	
	@RequestMapping("/keluarga/ubah")
	public String cariUbahKeluarga(Model model, @ModelAttribute("success") String success, @PathVariable Optional<String> nkk, KeluargaModel keluarga,
			HttpServletRequest request, @ModelAttribute("fail") String fail) {
		if(request.getMethod().equals("GET")) {
			if(!success.isEmpty()) {
				model.addAttribute("message", "Data dengan NKK " + success + " berhasil diubah.");
			}
			
			return "cariubahkeluarga";
		}else{
			return "redirect:/keluarga/ubah/"+ request.getParameter("nkk");
		}
	}
	
	@RequestMapping("/keluarga/ubah/{nkk}")
	public String ubahKeluarga(Model model,
			@ModelAttribute("fail") String fail, @PathVariable Optional<String> nkk, KeluargaModel keluarga,
			HttpServletRequest request, final RedirectAttributes redirectAttributes) {
		if(!nkk.isPresent()) {
			return "redirect:/keluarga/ubah";
		}else {
			if(request.getMethod().equals("POST")) {
				System.out.println(request.getMethod());
				System.out.println(keluarga);
				String kelurahanbaru = request.getParameter("kelurahanbaru");
				KelurahanModel kelurahan = SipenonDAO.selectKelurahanByName(kelurahanbaru);
				System.out.println("this" + kelurahanbaru);
				System.out.println(kelurahan);
//				KeluargaModel keluarga = SipenonDAO.selectKeluargaNKK(request.getParameter("id_keluarga"));
				if(kelurahan == null) {
					System.out.println("masuksini");
					redirectAttributes.addAttribute("fail", kelurahanbaru);
					System.out.println(keluarga.getNomor_kk());
					return "redirect:/keluarga/ubah/"+ nkk.get();
				}else {
					System.out.println("masuksini");
					keluarga.setId_kelurahan(kelurahan.getId());
					keluarga.setKelurahan(kelurahan);
					keluarga.setNomor_kk(nkk.get());
					System.out.println(keluarga);
					SipenonDAO.editKeluarga(keluarga);
					redirectAttributes.addAttribute("success", nkk.get());
					System.out.println(redirectAttributes);
					return "redirect:/keluarga/ubah";
				}
				
			}else {
				
				KeluargaModel change = SipenonDAO.selectKeluargaNKK(nkk.get());
				System.out.println(change);
				if(change == null) {
					model.addAttribute("message", "keluarga dengan NKK " + nkk.get() + " tidak ditemukan.");
					return "redirect:/keluarga/ubah";
				} else {
					if(!fail.isEmpty()) {
						model.addAttribute("message", "Nama Kelurahan " + fail + " Tidak ditemukan. Silahkan coba kembali.");
					}
					model.addAttribute("keluarga", change);
					return "ubahkeluarga";
				}
			}
		}
	}
	
	@RequestMapping("/penduduk/cari")
	public String searchPenduduk(@RequestParam(name="kt", required = false) String kt,
			@RequestParam(name="kc", required = false) String kc, 
			@RequestParam(name="kl", required = false) String kl,
			@ModelAttribute("success") String success, Model model) {
		if(kt== null) {
			model.addAttribute("kota", SipenonDAO.selectAllKota());
			return "caridatapenduduk";
		}else if(kt != null && kc == null) {
			model.addAttribute("kota", SipenonDAO.selectAllKota());
			model.addAttribute("kecamatan", SipenonDAO.selectKecamatanByKota(kt));
			return "caridatapenduduk";
		}else if(kt != null && kc != null && kl == null) {
			model.addAttribute("kota", SipenonDAO.selectAllKota());
			model.addAttribute("kecamatan", SipenonDAO.selectKecamatanByKota(kt));
			model.addAttribute("kelurahan", SipenonDAO.selectKelurahanByKecamatan(kc));
			return "caridatapenduduk";
		}else if(kt != null && kc != null && kl != null) {
			KotaModel kota = SipenonDAO.selectKota(Long.parseLong(kt));
			KecamatanModel kecamatan = SipenonDAO.selectKecamatan(Long.parseLong(kc));
			KelurahanModel kelurahan = SipenonDAO.selectKelurahan(Long.parseLong(kl));
			model.addAttribute("message","Lihat data penduduk di Kota "+kota.getNama_kota()+", Kecamatan "+kecamatan.getNama_kecamatan()+", Kelurahan "+kelurahan.getNama_kelurahan());
			model.addAttribute("penduduk", SipenonDAO.selectPendudukByKelurahan(kl));
			return "datapenduduk";
		}else {
			model.addAttribute("kota", SipenonDAO.selectAllKota());
			return "caridatapenduduk";
		}
			
		
	}
}
