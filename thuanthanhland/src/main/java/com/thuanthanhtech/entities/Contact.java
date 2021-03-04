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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contacts")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false)
	@NotNull
	@NotBlank(message = "Tên không được để trống")
	private String name;

	@Column(name = "email", nullable = false, unique = true)
	@Email(message = "Email không đúng định dạng")
	@NotNull
	@NotBlank(message = "Email không được để trống")
	private String email;

	@Column(name = "address")
	private String address;

	@Column(name = "phone", nullable = false)
	@NotBlank(message = "Số điện thoại không được để trống")
	@NotNull
	@Size(min = 10, max = 10, message = "Số điện thoại không đúng định dạng")
	@Pattern(regexp = "^(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Số điện thoại không đúng định dạng")
	private String phone;

	@Column(name = "content", columnDefinition = "LONGTEXT NOT NULL")
	private String content;

	@Column(name = "status", columnDefinition = "TINYINT(4) DEFAULT 0")
	private Integer status;

	private LocalDateTime created_at;
	private LocalDateTime updated_at;

	public Contact() {
		super();
	}

	

	public Contact(Integer id, String name, String email, String address, String phone, String content, Integer status,
			LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.content = content;
		this.status = status;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
