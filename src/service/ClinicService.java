package service;

import exception.PatientNotFoundException;
import exception.AppointmentException;
import model.*;
import repository.PatientRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class ClinicService {
    private final PatientRepository patientRepo = new PatientRepository();
    private final List<Doctor> doctors = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private static final int APPOINTMENT_BUFFER_MINUTES = 60;

    // Register Patient
    public void registerPatient(Patient patient) {
        patientRepo.addPatient(patient);
    }

    // Register Doctor
    public void registerDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null.");
        }
        if (getDoctorById(doctor.getId()) != null) {
            throw new IllegalArgumentException("Doctor ID already exists.");
        }
        doctors.add(doctor);
    }

    // Book Appointment
    public void bookAppointment(String appointmentId, String patientId, Doctor doctor, LocalDateTime dateTime) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            throw new AppointmentException("Appointment ID is required.");
        }
        if (doctor == null) {
            throw new AppointmentException("Doctor is required.");
        }
        if (dateTime == null) {
            throw new AppointmentException("Appointment date and time is required.");
        }

        Patient patient = patientRepo.getPatient(patientId);

        if (patient == null) {
            throw new PatientNotFoundException("Patient not registered.");
        }

        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new AppointmentException("Cannot book appointment in the past.");
        }

        // Check conflict with 10-minute buffer
        checkAppointmentConflict(doctor, dateTime);

        Appointment appointment = new Appointment(appointmentId.trim(), patient, doctor, dateTime);
        appointments.add(appointment);
    }

    /**
     * Checks if an appointment exists within ±10 minutes of the requested time for the same doctor.
     * @param doctor The doctor for the appointment
     * @param requestedTime The requested appointment time
     * @throws AppointmentException if a conflict is found
     */
    private void checkAppointmentConflict(Doctor doctor, LocalDateTime requestedTime) {
        for (Appointment existing : appointments) {
            if (existing.getDoctor().getId().equals(doctor.getId())) {
                LocalDateTime existingTime = existing.getDateTime();

                // Check if the requested time is within ±10 minutes of an existing appointment
                LocalDateTime bufferStart = existingTime.minusMinutes(APPOINTMENT_BUFFER_MINUTES);
                LocalDateTime bufferEnd = existingTime.plusMinutes(APPOINTMENT_BUFFER_MINUTES);

                if (!requestedTime.isBefore(bufferStart) && !requestedTime.isAfter(bufferEnd)) {
                    throw new AppointmentException(
                            "Doctor is not available at " + requestedTime.format(
                                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00"))
                                    + ". The doctor has an appointment at "
                                    + existingTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00"))
                                    + ". Please maintain at least " + APPOINTMENT_BUFFER_MINUTES + " minutes between appointments."
                    );
                }
            }
        }
    }

    // Add Medical Record
    public <T> void addMedicalRecord(String patientId, MedicalRecords<T> record) {
        Patient patient = patientRepo.getPatient(patientId);

        if (patient == null) {
            throw new PatientNotFoundException("Patient not found.");
        }

        patient.addMedicalRecord(record);
    }

    // Retrieve Records (Wildcard usage)
    public List<? extends MedicalRecords<?>> getPatientRecords(String patientId) {
        Patient patient = patientRepo.getPatient(patientId);

        if (patient == null) {
            throw new PatientNotFoundException("Patient not found.");
        }

        return patient.getMedicalRecords();
    }

    public Doctor getDoctorById(String doctorId) {
        if (doctorId == null || doctorId.trim().isEmpty()) {
            return null;
        }

        for (Doctor d : doctors) {
            if (d.getId().equalsIgnoreCase(doctorId.trim())) {
                return d;
            }
        }
        return null;
    }

    public List<Doctor> getAllDoctors() {
        return Collections.unmodifiableList(doctors);
    }

    public List<Appointment> getAllAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public Patient getPatientById(String patientId) {
        return patientRepo.getPatient(patientId);
    }
}
