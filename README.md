# Kurumsal Proje Yönetim Sistemi 🚀

Bu proje, şirketlerin projelerini, çalışanlarını ve yetkilendirmelerini merkezi olarak yönetmesini sağlayan kurumsal bir web uygulamasıdır. Backend tarafı **Spring Boot**, frontend tarafı ise **React** kullanılarak geliştirilmiş modern bir Full-Stack mimariye sahiptir.

## Ana Özellikler ✨
- **Güvenli Kimlik Doğrulama:** JWT tabanlı güvenli oturum yönetimi ve rol tabanlı erişim (Role-Based Access Control).
- **Proje ve Dosya Yönetimi:** Proje oluşturma, durum (Yeni, Devam Ediyor, Tamamlandı) takibi ve projelere güvenli dosya yükleme (Whitelist destekli).
- **Çalışan Entegrasyonu:** Projelerde çalışan (Employee) atama ve çıkarma işlemleri.
- **İstatistik Dashboard'u:** Sistem üzerindeki tüm varlıkların (Kullanıcı, Proje, Dosya) sayısını raporlayan gösterge paneli.
- **Güçlü Hata Yönetimi:** Spring Validation ve Global Exception Handler ile 400/500 tarzı hataların kontrollü JSON yanıtlarına dönüştürülmesi.
- **DTO Mimarisi:** İstemci tarafına gönderilen veriler (şifreler vb.) DTO (Data Transfer Object) yapısı ile güvence altına alınmıştır.

---

## 🛠️ Teknolojiler
**Backend:**
- Java 21 & Spring Boot 3.2
- Spring Security & JWT (JSON Web Token)
- Spring Data JPA & Hibernate
- PostgreSQL Veritabanı
- Maven (Bağımlılık Yönetimi)

**Frontend:**
- React.js 18
- React Router DOM (Yönlendirmeler)
- Axios (Merkezi BaseURL & Token interceptor yapılandırması)

**Dağıtım (Deployment):**
- Docker & Docker Compose (`docker-compose.yml`)

---

## 📦 Kurulum ve Çalıştırma

### Gereksinimler
- **Java 21**, **Node.js (v16+)**, **Docker** (Tercihen)
- Açık bir PostgreSQL sunucusu veya Docker.

### 1. Docker ile Hızlı Başlangıç (Önerilen)
Projenin ana dizininde aşağıdaki komutu çalıştırarak veritabanının otomatik yapılandırılmasını sağlayabilirsiniz:
```bash
docker-compose up -d
```
> **Not:** `.env` dosyanızda veya sistem environment variable'larınızda `SPRING_DATASOURCE_PASSWORD` ve `JWT_SECRET` değerlerinin atanmış olduğundan emin olun.

### 2. Manuel Kurulum

#### Backend (Spring Boot)
1. `src/main/resources/application.properties` dosyasında veritabanı ayarlarını PostgreSQL sunucunuza göre uyarlayın.
2. Proje dizininde Maven ile projeyi derleyin ve başlatın:
```bash
mvn clean install
mvn spring-boot:run
```
> *Sunucu varsayılan olarak `http://localhost:8080` üzerinde çalışacaktır.*

#### Frontend (React)
1. `frontend` klasörüne gidin.
2. Bağımlılıkları yükleyin ve başlatın:
```bash
cd frontend
npm install
npm start
```
> *React uygulaması varsayılan olarak `http://localhost:3000` üzerinde açılacaktır.*

---

## 🔒 Varsayılan Yönetici (Admin) Hesabı
Sistemi ilk kez başlattığınızda otomatik olarak bir Admin hesabı oluşur:
- **Kullanıcı Adı:** `admin`
- **Şifre:** `admin1907` *(Lütfen ilk girişte şifrenizi değiştirin)*

Herhangi bir sorun yaşarsanız veya gelişime katkı sağlamak isterseniz Issue açabilirsiniz. İyi çalışmalar! 💻