package com.thuanthanhtech.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.thuanthanhtech.entities.UserValidator.saveValidation;
import com.thuanthanhtech.entities.UserValidator.updateValidation;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
	@NotBlank(message = "Tên tài khoản không được để trống", groups = {UserValidator.saveValidation.class, UserValidator.updateValidation.class})
	private String name;

	@Column(name = "email", columnDefinition = "VARCHAR(255) NOT NULL", unique = true)
	@Email(message = "Địa chỉ email không hợp lệ", groups = {UserValidator.saveValidation.class, UserValidator.updateValidation.class})
	@NotBlank(message = "Địa chỉ email không được để trống", groups = {UserValidator.saveValidation.class, UserValidator.updateValidation.class})
	private String email;

	@Column(name = "role", columnDefinition = "TINYINT(4) DEFAULT 0") // 0: normal; 1: administrator
	private Integer role;

	@Column(name = "address")
	private String address;

	@Column(name = "password", columnDefinition = "VARCHAR(255) NOT NULL")
	@Size(min = 8, max = 32, message = "Mật khẩu phải có ít nhất 8 kí tự", groups = {UserValidator.saveValidation.class})
	@NotBlank(message = "Mật khẩu không được để trống", groups = {UserValidator.saveValidation.class})
	private String password;

	@Column(name = "phone", columnDefinition = "VARCHAR(10) NOT NULL")
	@NotBlank(message = "Số điện thoại không được để trống", groups = {UserValidator.saveValidation.class, UserValidator.updateValidation.class})
	@Size(min = 10, max = 10, message = "Số điện thoại không đúng định dạng", groups = {UserValidator.saveValidation.class, UserValidator.updateValidation.class})
	@Pattern(regexp = "^(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Số điện thoại không đúng định dạng")
	private String phone;

	@Column(name = "avatar")
	private String avatar;

	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	public User() {
		super();
	}

	

	public User(Integer id,
			@NotBlank(message = "Tên tài khoản không được để trống", groups = { saveValidation.class,
					updateValidation.class }) String name,
			@Email(message = "Địa chỉ email không hợp lệ", groups = { saveValidation.class,
					updateValidation.class }) @NotBlank(message = "Địa chỉ email không được để trống", groups = {
							saveValidation.class, updateValidation.class }) String email,
			Integer role, String address,
			@Size(min = 8, max = 32, message = "Mật khẩu phải có ít nhất 8 kí tự", groups = saveValidation.class) @NotBlank(message = "Mật khẩu không được để trống", groups = saveValidation.class) String password,
			@NotBlank(message = "Số điện thoại không được để trống", groups = { saveValidation.class,
					updateValidation.class }) @Size(min = 10, max = 10, message = "Số điện thoại không đúng định dạng", groups = {
							saveValidation.class,
							updateValidation.class }) @Pattern(regexp = "^(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Số điện thoại không đúng định dạng") String phone,
			String avatar, LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.address = address;
		this.password = password;
		this.phone = phone;
		this.avatar = avatar;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

	@PrePersist
	public void prePersist() {
		this.created_at = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updated_at = LocalDateTime.now();
	}

}
