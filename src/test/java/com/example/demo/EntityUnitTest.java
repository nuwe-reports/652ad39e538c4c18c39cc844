package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

    DateTimeFormatter formatter;
    
    @BeforeAll
    void initialize(){
        formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    }

    @Test
    void shouldPersistDoctor(){
        Doctor d1 = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        
        Object id1 = entityManager.persistAndGetId(d1);
        Doctor savedDoctor1 = entityManager.find(Doctor.class, id1);
        assertThat(peopleStructuralEquals(d1, savedDoctor1)).isTrue();
        
        d1 = new Doctor();
        d1.setFirstName("David");
        d1.setLastName("Gómez");
        d1.setAge(32);
        d1.setEmail("d.gomez@hospital.accwe");

        Object id2 = entityManager.persistAndGetId(d1);
        Doctor savedDoctor2 = entityManager.find(Doctor.class, id2);
        assertThat(peopleStructuralEquals(d1, savedDoctor2)).isTrue();
    }
    
    @Test
    void shouldPersistPatient(){
        Patient p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        
        Object id1 = entityManager.persistAndGetId(p1);
        Patient savedPatient1 = entityManager.find(Patient.class, id1);
        assertThat(peopleStructuralEquals(p1, savedPatient1)).isTrue();
        
        p1 = new Patient();
        p1.setFirstName("David");
        p1.setLastName("Gómez");
        p1.setAge(32);
        p1.setEmail("d.gomez@hospital.accwe");

        Object id2 = entityManager.persistAndGetId(p1);
        Patient savedPatient2 = entityManager.find(Patient.class, id2);
        assertThat(peopleStructuralEquals(p1, savedPatient2)).isTrue();
    }
    
    @Test
    void shouldPersistRoom(){
        Room r1 = new Room();
        
        assertThat(r1.getRoomName()).isNull();
        
        r1 = new Room("Dermatology");

        Object id1 = entityManager.persistAndGetId(r1);
        Room savedRoom1 = entityManager.find(Room.class, id1);
        
        assertThat(r1.getRoomName()).isEqualTo(savedRoom1.getRoomName());
    }

    @Test
    void shouldPersistAppointment(){
        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);
        
        Appointment a1 = new Appointment(
            new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"),
            new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe"),
            new Room("Dermatology"),
            startsAt,
            finishesAt
        );
        
        Object id1 = entityManager.persistAndGetId(a1);
        Appointment savedAppointment1 = entityManager.find(Appointment.class, id1);
        
        assertThat(peopleStructuralEquals(a1.getDoctor(), savedAppointment1.getDoctor())).isTrue();
        assertThat(peopleStructuralEquals(a1.getPatient(), savedAppointment1.getPatient())).isTrue();
        assertThat(a1.getRoom().getRoomName()).isEqualTo(savedAppointment1.getRoom().getRoomName());
        assertThat(a1.getStartsAt()).isEqualTo(savedAppointment1.getStartsAt());
        assertThat(a1.getFinishesAt()).isEqualTo(savedAppointment1.getFinishesAt());

        a1 = new Appointment();
        a1.setPatient(new Patient("David", "Gómez", 28, "dgr94@email.com"));
        a1.setDoctor(new Doctor("Francisca", "Martínez", 45, "f.martinez@hospital.accwe"));
        a1.setRoom(new Room("Cardiology"));
        a1.setStartsAt(startsAt);
        a1.setFinishesAt(finishesAt);

        Object id2 = entityManager.persistAndGetId(a1);
        Appointment savedAppointment2 = entityManager.find(Appointment.class, id2);
        
        assertThat(peopleStructuralEquals(a1.getDoctor(), savedAppointment2.getDoctor())).isTrue();
        assertThat(peopleStructuralEquals(a1.getPatient(), savedAppointment2.getPatient())).isTrue();
        assertThat(a1.getRoom().getRoomName()).isEqualTo(savedAppointment2.getRoom().getRoomName());
        assertThat(a1.getStartsAt()).isEqualTo(savedAppointment2.getStartsAt());
        assertThat(a1.getFinishesAt()).isEqualTo(savedAppointment2.getFinishesAt());

    }

    @Test
    void appointmentsOverlapsWhenStartAtTheSameTime() {
        Appointment a1 = new Appointment(
            new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"),
            new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("19:30 24/04/2023", formatter),
            LocalDateTime.parse("20:30 24/04/2023", formatter)
        );
        Appointment a2 = new Appointment(
            new Patient("Another Patient", "Lastname", 25, "another@email.com"),
            new Doctor("Another Doctor", "DocLastName", 30, "another.doctor@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("19:30 24/04/2023", formatter),
            LocalDateTime.parse("20:30 24/04/2023", formatter)
        );

        assertThat(a1.overlaps(a2)).isTrue();
    }

    @Test
    void appointmentsOverlapsWhenFinishAtTheSameTime() {
        Appointment a1 = new Appointment(
            new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"),
            new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("20:00 24/04/2023", formatter),
            LocalDateTime.parse("20:30 24/04/2023", formatter)
        );
        Appointment a2 = new Appointment(
            new Patient("Another Patient", "Lastname", 25, "another@email.com"),
            new Doctor("Another Doctor", "DocLastName", 30, "another.doctor@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("19:30 24/04/2023", formatter),
            LocalDateTime.parse("20:30 24/04/2023", formatter)
        );

        assertThat(a1.overlaps(a2)).isTrue();
    }

    @Test
    void appointmentsOverlapsWhenOneBeginsBeforeTheOtherEnds() {
        Appointment a1 = new Appointment(
            new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"),
            new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("19:00 24/04/2023", formatter),
            LocalDateTime.parse("20:00 24/04/2023", formatter)
        );
        Appointment a2 = new Appointment(
            new Patient("Another Patient", "Lastname", 25, "another@email.com"),
            new Doctor("Another Doctor", "DocLastName", 30, "another.doctor@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("19:30 24/04/2023", formatter),
            LocalDateTime.parse("20:30 24/04/2023", formatter)
        );

        
        assertThat(a1.overlaps(a2)).isTrue();
        assertThat(a2.overlaps(a1)).isTrue();
    }

    @Test
    void appointmentsDoesNotOverlapsBaseCase() {
        Appointment a1 = new Appointment(
            new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"),
            new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("19:00 24/04/2023", formatter),
            LocalDateTime.parse("20:00 24/04/2023", formatter)
        );
        Appointment a2 = new Appointment(
            new Patient("Another Patient", "Lastname", 25, "another@email.com"),
            new Doctor("Another Doctor", "DocLastName", 30, "another.doctor@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("20:00 24/04/2023", formatter),
            LocalDateTime.parse("21:00 24/04/2023", formatter)
        );

        assertThat(a1.overlaps(a2)).isFalse();
    }

    @Test
    void appointmentsInDifferentRoomsDoesNotOverlaps() {
        Appointment a1 = new Appointment(
            new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"),
            new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("19:00 24/04/2023", formatter),
            LocalDateTime.parse("20:00 24/04/2023", formatter)
        );
        Appointment a2 = new Appointment(
            new Patient("Another Patient", "Lastname", 25, "another@email.com"),
            new Doctor("Another Doctor", "DocLastName", 30, "another.doctor@hospital.accwe"),
            new Room("Cardiology"), // Different room
            LocalDateTime.parse("19:30 24/04/2023", formatter),
            LocalDateTime.parse("20:30 24/04/2023", formatter)
        );

        assertThat(a1.overlaps(a2)).isFalse();
    }

    @Test
    void appointmentsIdCanBeSet() {        
        Appointment appointment = new Appointment(
            new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com"),
            new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe"),
            new Room("Dermatology"),
            LocalDateTime.parse("19:00 24/04/2023", formatter),
            LocalDateTime.parse("20:00 24/04/2023", formatter)
        );

        appointment.setId(1);

        assertThat(appointment.getId()).isEqualTo(1);
    }

    @Test
    void doctorsIdCanBeSet() {
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        doctor.setId(1);

        assertThat(doctor.getId()).isEqualTo(1);
    }

    @Test
    void patientsIdCanBeSet() {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        patient.setId(1);

        assertThat(patient.getId()).isEqualTo(1);
    }

    private boolean peopleStructuralEquals(Person p1, Person p2){
        return (
            p1.getAge() == p2.getAge() &&
            p1.getEmail() == p2.getEmail() &&
            p1.getFirstName() == p2.getFirstName() &&
            p1.getLastName() == p2.getLastName()
        );
    }
}
