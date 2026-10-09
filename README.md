# SistemaRural-PE

Este es un proyecto en Java hecho para el puesto de salud **Nueva Esperanza** en Marcabal Grande. El objetivo es ayudar a gestionar el registro de pacientes, las citas, las atenciones médicas y el inventario de medicamentos de forma sencilla.

---

## ¿Qué tiene el proyecto?

- **Manejo de Pacientes y Citas:** Registra los datos de los pacientes, agenda citas y guarda sus atenciones médicas.
- **Inventario de Medicamentos:** Lleva el control de las medicinas y avisa si queda poco stock.
- **Protección de Datos:** Los datos importantes como el DNI no se muestran completos por privacidad, y las contraseñas están guardadas de forma segura.
- **Reportes:** Genera reportes básicos de las atenciones y del stock de medicamentos.
- **Pruebas JUnit:** Incluye un archivo con pruebas unitarias para comprobar que todo funcione bien.

---

## Estructura del Código

El código está ordenado por paquetes dentro de la carpeta `src`:

```text
SistemaRuralPE/
└── src/
    ├── aplicacion/
    │   └── Principal.java                  # Clase principal con el método main
    ├── modelo/
    │   ├── Persona.java                    # Clase base de la que heredan Paciente y PersonalSalud
    │   ├── Paciente.java
    │   ├── PersonalSalud.java
    │   ├── Atencion.java
    │   ├── Cita.java
    │   ├── HistorialClinico.java
    │   ├── Medicamento.java
    │   └── InventarioMedicamentos.java
    ├── servicios/
    │   ├── GestionPacientes.java           # Lógica para buscar y filtrar pacientes
    │   ├── GestionCitas.java               # Lógica para programar citas
    │   ├── Reporte.java
    │   ├── ReporteAtenciones.java
    │   ├── ReporteInventario.java
    │   └── ReporteFactory.java
    ├── excepciones/
    │   ├── SistemaRuralException.java      # Excepción personalizada
    │   ├── StockInsuficienteException.java
    │   ├── DatosInvalidosException.java
    │   └── AccesoDenegadoException.java
    └── test/
        └── SistemaRuralPETest.java         # Pruebas con JUnit 5
