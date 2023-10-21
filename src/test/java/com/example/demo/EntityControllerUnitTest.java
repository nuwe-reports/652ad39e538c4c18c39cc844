
package com.example.demo;

import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest{

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String baseUrl = "/api/doctors/";

    @Test
    void shouldGetNoDoctors() throws Exception{
        List<Doctor> doctors = new ArrayList<Doctor>();
        
        when(doctorRepository.findAll()).thenReturn(doctors);
        
        mockMvc.perform(get(baseUrl))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetTwoDoctors() throws Exception{
        Doctor d1 = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor d2 = new Doctor ("Miren", "Iniesta", 24, "m.iniesta@hospital.accwe");

        List<Doctor> doctors = new ArrayList<Doctor>();
        doctors.add(d1);
        doctors.add(d2);

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get(baseUrl))
            .andExpect(status().isOk());
    }

    @Test
    void shouldGetDoctorById() throws Exception{
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        doctor.setId(1);

        Optional<Doctor> optional = Optional.of(doctor);

        assertThat(optional).isPresent();
        assertThat(optional.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(1);

        when(doctorRepository.findById(doctor.getId())).thenReturn(optional);

        mockMvc.perform(get(baseUrl + doctor.getId()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAnyDoctorById() throws Exception{
        long id = 7;

        mockMvc.perform(get(baseUrl + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteDoctorById() throws Exception{
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        doctor.setId(1);

        Optional<Doctor> optional = Optional.of(doctor);

        assertThat(optional).isPresent();
        assertThat(optional.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(1);

        when(doctorRepository.findById(doctor.getId())).thenReturn(optional);

        mockMvc.perform(delete(baseUrl + doctor.getId()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteDoctor() throws Exception{
        long id = 9;

        mockMvc.perform(delete(baseUrl + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllDoctors() throws Exception{
        mockMvc.perform(delete(baseUrl))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateDoctor() throws Exception{
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        mockMvc.perform(post("/api/doctor")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(doctor)))
            .andExpect(status().isCreated());
    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest{

    @MockBean
    private PatientRepository patientRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String baseUrl = "/api/patients/";

    @Test
    void shouldGetNoPatients() throws Exception{
        List<Patient> patients = new ArrayList<Patient>();

        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get(baseUrl))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetTwoPatients() throws Exception{
        Patient p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient p2 = new Patient("Paulino", "Antunez", 37, "p.antunez@email.com");

        List<Patient> patients = new ArrayList<Patient>();
        patients.add(p1);
        patients.add(p2);

        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get(baseUrl))
            .andExpect(status().isOk());
    }

    @Test
    void shouldGetPatientById() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        patient.setId(1);

        Optional<Patient> optional = Optional.of(patient);

        assertThat(optional).isPresent();
        assertThat(optional.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(1);

        when(patientRepository.findById(patient.getId())).thenReturn(optional);

        mockMvc.perform(get(baseUrl + patient.getId()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAnyPatientById() throws Exception{
        long id = 3;

        mockMvc.perform(get(baseUrl + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeletePatientById() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        patient.setId(1);

        Optional<Patient> optional = Optional.of(patient);

        assertThat(optional).isPresent();
        assertThat(optional.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(1);

        when(patientRepository.findById(patient.getId())).thenReturn(optional);

        mockMvc.perform(delete(baseUrl + patient.getId()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeletePatient() throws Exception{
        long id = 11;

        mockMvc.perform(delete(baseUrl + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllPatients() throws Exception{
        mockMvc.perform(delete(baseUrl))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreatePatient() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        mockMvc.perform(post("/api/patient")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(patient)))
            .andExpect(status().isCreated());
    }
}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest{

    @MockBean
    private RoomRepository roomRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String baseUrl = "/api/rooms/";

    @Test
    void shouldGetNoRooms() throws Exception{
        List<Room> rooms = new ArrayList<Room>();

        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get(baseUrl))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetTwoRooms() throws Exception{
        Room r1 = new Room("Dermatology");
        Room r2 = new Room("Cardiology");

        List<Room> rooms = new ArrayList<Room>();
        rooms.add(r1);
        rooms.add(r2);

        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get(baseUrl))
            .andExpect(status().isOk());
    }

    @Test
    void shouldGetRoomByRoomName() throws Exception{
        final String roomName = "Dermatology";
        Room room = new Room(roomName);

        Optional<Room> optional = Optional.of(room);

        assertThat(optional).isPresent();
        assertThat(optional.get().getRoomName()).isEqualTo(room.getRoomName());
        assertThat(room.getRoomName()).isEqualTo(roomName);

        when(roomRepository.findByRoomName(room.getRoomName())).thenReturn(optional);

        mockMvc.perform(get(baseUrl + room.getRoomName()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAnyRoomByRoomName() throws Exception{
        final String roomName = "Cardiology";

        mockMvc.perform(get(baseUrl + roomName))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteRoomByRoomName() throws Exception{
        final String roomName = "Dermatology";
        Room room = new Room(roomName);

        Optional<Room> optional = Optional.of(room);

        assertThat(optional).isPresent();
        assertThat(optional.get().getRoomName()).isEqualTo(room.getRoomName());
        assertThat(room.getRoomName()).isEqualTo(roomName);

        when(roomRepository.findByRoomName(room.getRoomName())).thenReturn(optional);

        mockMvc.perform(delete(baseUrl + room.getRoomName()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteRoom() throws Exception{
        final String roomName = "Cardiology";

        mockMvc.perform(delete(baseUrl + roomName))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllRooms() throws Exception{
        mockMvc.perform(delete(baseUrl))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateRoom() throws Exception{
        Room room = new Room("Cardiology");

        mockMvc.perform(post("/api/room")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(room)))
            .andExpect(status().isCreated());
    }
}
