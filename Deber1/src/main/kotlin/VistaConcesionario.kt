import java.awt.FlowLayout
import java.awt.event.ActionListener
import java.text.SimpleDateFormat
import javax.swing.*
import javax.swing.event.ListSelectionListener

class VistaConcesionario(titulo:String):JFrame(titulo) {
    var btnNuevo:JButton
    var btnActualizar:JButton
    var btnEliminar:JButton
    var txtNombre:JTextField
    var txtSuperficie:JTextField
    var txtNumConcesionario:JTextField
    var txtFechaApertura:JTextField
    var txtAbierto:JTextField
    var listaConsesionarios:JList<Concesionario>
    var concesionarios:ArrayList<Concesionario>?
    val accesoArchivos:FileDirectAccess<Concesionario,Int>
    init {
        txtNombre = JTextField()
        txtSuperficie = JTextField()
        txtNumConcesionario = JTextField()
        txtFechaApertura = JTextField()
        txtAbierto = JTextField()
        btnNuevo = JButton("Añadir")
        btnActualizar = JButton("Actualizar")
        btnEliminar = JButton("Eliminar")
        layout = FlowLayout()
        accesoArchivos = FileDirectAccess<Concesionario, Int>("concesionario.txt", Concesionario::class.java)
        listaConsesionarios = JList()
        concesionarios = accesoArchivos.listar()
        actualizarLista()
        btnNuevo.addActionListener(ActionListener {
            var nuevo = Concesionario(txtNombre.text,txtAbierto.text.toBoolean(),
                txtSuperficie.text.toDouble(),txtNumConcesionario.text.toInt(),
                SimpleDateFormat("dd/mm/yy").parse(txtFechaApertura.text))
            accesoArchivos.crear(nuevo)
        })
        listaConsesionarios.visibleRowCount = 3
        listaConsesionarios.addListSelectionListener(ListSelectionListener(){
                var instancia = concesionarios?.get(listaConsesionarios.selectedIndex)
                txtNombre.text = instancia?.nombreConcesionario
                txtAbierto.text = instancia?.estaAbierto.toString()
                txtFechaApertura.text = SimpleDateFormat("dd/mm/yyyy").format(instancia?.fechaApertura)
                txtSuperficie.text = instancia?.superficie.toString()
                txtNumConcesionario.text = instancia?.numConsesionario.toString()
        })
        add(JScrollPane(listaConsesionarios))
        var panel = JPanel()
        panel.add(JLabel("Numero del consesionario"))
        panel.add(txtNumConcesionario)
        add(panel)
        //add(txtNumConcesionario)
        add(JLabel("Nombre del consesionario"))
        add(txtNombre)
        add(JLabel("Abierto:"))
        add(txtAbierto)
        add(JLabel("Superficie:"))
        add(txtSuperficie)
        add(JLabel("Fecha de apertura"))
        add(txtFechaApertura)
        add(btnNuevo)
        add(btnActualizar)
        add(btnEliminar)
    }

    fun actualizarLista(){
        concesionarios = accesoArchivos.listar()
        if (concesionarios != null) {
            val arr = concesionarios!!.toTypedArray()
            listaConsesionarios = JList(arr)
        } else {
            listaConsesionarios = JList()
        }
    }
}