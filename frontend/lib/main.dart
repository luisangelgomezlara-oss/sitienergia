import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'api.dart';

void main() => runApp(const SitiEnergiaApp());

class SitiEnergiaApp extends StatelessWidget {
  const SitiEnergiaApp({super.key});
  @override
  Widget build(BuildContext context) => MaterialApp(
        title: 'SitiEnergia',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xff176b87)), useMaterial3: true),
        home: const LoginPage(),
      );
}

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});
  @override State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final email = TextEditingController(text: 'admin@sitienergia.com');
  final password = TextEditingController();
  bool loading = false;
  String? error;

  Future<void> login() async {
    setState(() { loading = true; error = null; });
    try {
      final session = await Api.login(email.text.trim(), password.text);
      if (!mounted) return;
      Navigator.pushReplacement(context, MaterialPageRoute(builder: (_) => HomePage(session: session)));
    } catch (e) {
      if (mounted) setState(() => error = e.toString().replaceFirst('Exception: ', ''));
    }
    if (mounted) setState(() => loading = false);
  }

  @override
  Widget build(BuildContext context) => Scaffold(
        body: Center(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(28),
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 430),
              child: Column(crossAxisAlignment: CrossAxisAlignment.stretch, children: [
                const Icon(Icons.electrical_services, size: 56, color: Color(0xff176b87)),
                const SizedBox(height: 18),
                const Text('SitiEnergia', textAlign: TextAlign.center, style: TextStyle(fontSize: 32, fontWeight: FontWeight.bold)),
                const Text('Sistema Integrado de Tareas e Incidencias Energéticas', textAlign: TextAlign.center),
                const SizedBox(height: 36),
                TextField(controller: email, keyboardType: TextInputType.emailAddress, decoration: const InputDecoration(labelText: 'Correo electrónico', prefixIcon: Icon(Icons.mail_outline), border: OutlineInputBorder())),
                const SizedBox(height: 14),
                TextField(controller: password, obscureText: true, decoration: const InputDecoration(labelText: 'Contraseña', prefixIcon: Icon(Icons.lock_outline), border: OutlineInputBorder())),
                if (error != null) Padding(padding: const EdgeInsets.only(top: 14), child: Text(error!, style: TextStyle(color: Theme.of(context).colorScheme.error))),
                const SizedBox(height: 22),
                FilledButton.icon(onPressed: loading ? null : login, icon: const Icon(Icons.login), label: Text(loading ? 'Ingresando...' : 'Iniciar sesión'), style: FilledButton.styleFrom(padding: const EdgeInsets.all(16))),
                TextButton(onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (_) => const RegisterPage())), child: const Text('¿No tienes cuenta? Regístrate')),
              ]),
            ),
          ),
        ),
      );
}

class RegisterPage extends StatefulWidget {
  const RegisterPage({super.key});
  @override State<RegisterPage> createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  final name = TextEditingController();
  final email = TextEditingController();
  final phone = TextEditingController();
  final password = TextEditingController();
  bool loading = false;
  String? error;

  Future<void> register() async {
    if (name.text.trim().isEmpty || !email.text.contains('@') || password.text.length < 6) {
      setState(() => error = 'Completa los campos. La contraseña debe tener mínimo 6 caracteres.');
      return;
    }
    setState(() { loading = true; error = null; });
    try {
      await Api.register(name.text.trim(), email.text.trim(), phone.text.trim(), password.text);
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Cuenta creada. Ya puedes iniciar sesión.')));
      Navigator.pop(context);
    } catch (e) {
      if (mounted) setState(() => error = e.toString().replaceFirst('Exception: ', ''));
    }
    if (mounted) setState(() => loading = false);
  }

  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(title: const Text('Crear cuenta')),
        body: ListView(padding: const EdgeInsets.all(24), children: [
          TextField(controller: name, decoration: const InputDecoration(labelText: 'Nombre completo', border: OutlineInputBorder())),
          const SizedBox(height: 14),
          TextField(controller: email, keyboardType: TextInputType.emailAddress, decoration: const InputDecoration(labelText: 'Correo electrónico', border: OutlineInputBorder())),
          const SizedBox(height: 14),
          TextField(controller: phone, keyboardType: TextInputType.phone, decoration: const InputDecoration(labelText: 'Teléfono', border: OutlineInputBorder())),
          const SizedBox(height: 14),
          TextField(controller: password, obscureText: true, decoration: const InputDecoration(labelText: 'Contraseña', border: OutlineInputBorder())),
          if (error != null) Padding(padding: const EdgeInsets.only(top: 14), child: Text(error!, style: TextStyle(color: Theme.of(context).colorScheme.error))),
          const SizedBox(height: 20),
          FilledButton(onPressed: loading ? null : register, child: Text(loading ? 'Creando...' : 'Registrarme')),
        ],
        ),
      );
}

class HomePage extends StatefulWidget {
  final Session session;
  const HomePage({super.key, required this.session});
  @override State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int tab = 0;
  void logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.clear();
    if (mounted) Navigator.pushReplacement(context, MaterialPageRoute(builder: (_) => const LoginPage()));
  }

  @override
  Widget build(BuildContext context) {
    final admin = widget.session.rol == 'ADMIN';
    final pages = <Widget>[Overview(session: widget.session)];
    if (admin) {
      pages.add(CategoriesPage(session: widget.session));
      pages.add(UsersPage(session: widget.session));
    }
    return Scaffold(
      appBar: AppBar(title: Text(tab == 0 ? 'Resumen' : tab == 1 ? 'Categorías' : 'Usuarios'), actions: [IconButton(onPressed: logout, tooltip: 'Cerrar sesión', icon: const Icon(Icons.logout))]),
      body: pages[tab],
      bottomNavigationBar: NavigationBar(selectedIndex: tab, onDestinationSelected: (value) => setState(() => tab = value), destinations: [
        const NavigationDestination(icon: Icon(Icons.dashboard_outlined), label: 'Resumen'),
        if (admin) const NavigationDestination(icon: Icon(Icons.category_outlined), label: 'Categorías'),
        if (admin) const NavigationDestination(icon: Icon(Icons.people_outline), label: 'Usuarios'),
      ]),
    );
  }
}

class Overview extends StatelessWidget {
  final Session session;
  const Overview({super.key, required this.session});
  @override
  Widget build(BuildContext context) => ListView(padding: const EdgeInsets.all(20), children: [
        Text('Hola, ${session.nombre}', style: Theme.of(context).textTheme.headlineSmall),
        const SizedBox(height: 6),
        const Text('Sistema Integrado de Tareas e Incidencias Energéticas'),
        const SizedBox(height: 28),
        Card(child: ListTile(leading: const Icon(Icons.security), title: const Text('Sesión segura'), subtitle: Text('Rol actual: ${session.rol}'))),
        const Card(child: ListTile(leading: Icon(Icons.rule), title: Text('SitiEnergia'), subtitle: Text('Gestión de incidencias del sector eléctrico'))),
      ]);
}

class CategoriesPage extends StatefulWidget {
  final Session session;
  const CategoriesPage({super.key, required this.session});
  @override State<CategoriesPage> createState() => _CategoriesPageState();
}

class _CategoriesPageState extends State<CategoriesPage> {
  List<dynamic> items = [];
  bool loading = true;
  @override void initState() { super.initState(); refresh(); }
  void message(Object error) => ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(error.toString().replaceFirst('Exception: ', ''))));
  Future<void> refresh() async {
    try { final data = await Api.categories(widget.session.token); if (mounted) setState(() { items = data; loading = false; }); }
    catch (e) { if (mounted) { setState(() => loading = false); message(e); } }
  }
  Future<void> form({Map<String, dynamic>? item}) async {
    final name = TextEditingController(text: item?['nombre'] ?? '');
    final area = TextEditingController(text: item?['area'] ?? '');
    final ok = await showDialog<bool>(context: context, builder: (_) => AlertDialog(title: Text(item == null ? 'Nueva categoría' : 'Editar categoría'), content: Column(mainAxisSize: MainAxisSize.min, children: [TextField(controller: name, decoration: const InputDecoration(labelText: 'Nombre')), TextField(controller: area, decoration: const InputDecoration(labelText: 'Área'))]), actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancelar')), FilledButton(onPressed: () => Navigator.pop(context, name.text.trim().isNotEmpty && area.text.trim().isNotEmpty), child: const Text('Guardar'))]));
    if (ok != true) return;
    try { if (item == null) { await Api.createCategory(widget.session.token, name.text, area.text); } else { await Api.updateCategory(widget.session.token, item['id'], name.text, area.text); } refresh(); } catch (e) { message(e); }
  }
  Future<void> deactivateCategory(Map<String, dynamic> item) async {
    final ok = await showDialog<bool>(context: context, builder: (_) => AlertDialog(title: const Text('Desactivar categoría'), content: Text('¿Desactivar "${item['nombre']}"?'), actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancelar')), FilledButton(onPressed: () => Navigator.pop(context, true), child: const Text('Desactivar'))]));
    if (ok == true) { try { await Api.deleteCategory(widget.session.token, item['id']); refresh(); } catch (e) { message(e); } }
  }
  @override
  Widget build(BuildContext context) => loading ? const Center(child: CircularProgressIndicator()) : RefreshIndicator(onRefresh: refresh, child: ListView(padding: const EdgeInsets.all(16), children: [
        Align(alignment: Alignment.centerRight, child: FilledButton.icon(onPressed: () => form(), icon: const Icon(Icons.add), label: const Text('Nueva categoría'))),
        const SizedBox(height: 10),
        ...items.map((item) => Card(child: ListTile(leading: const Icon(Icons.label_outline), title: Text(item['nombre']), subtitle: Text(item['area']), trailing: Row(mainAxisSize: MainAxisSize.min, children: [IconButton(tooltip: 'Editar', onPressed: () => form(item: item), icon: const Icon(Icons.edit_outlined)), IconButton(tooltip: 'Desactivar', onPressed: item['activa'] == true ? () => deactivateCategory(item) : null, icon: const Icon(Icons.delete_outline))])))),
      ]));
}

class UsersPage extends StatefulWidget {
  final Session session;
  const UsersPage({super.key, required this.session});
  @override State<UsersPage> createState() => _UsersPageState();
}

class _UsersPageState extends State<UsersPage> {
  List<dynamic> items = [];
  bool loading = true;
  @override void initState() { super.initState(); refresh(); }
  void message(Object error) => ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(error.toString().replaceFirst('Exception: ', ''))));
  Future<void> refresh() async {
    try { final data = await Api.users(widget.session.token); if (mounted) setState(() { items = data; loading = false; }); }
    catch (e) { if (mounted) { setState(() => loading = false); message(e); } }
  }
  Future<void> edit(Map<String, dynamic> item) async {
    final name = TextEditingController(text: item['nombre']);
    final phone = TextEditingController(text: item['telefono'] ?? '');
    String role = item['rol'];
    bool active = item['activo'] == true;
    final ok = await showDialog<bool>(context: context, builder: (_) => StatefulBuilder(builder: (context, setDialog) => AlertDialog(title: const Text('Editar usuario'), content: Column(mainAxisSize: MainAxisSize.min, children: [TextField(controller: name, decoration: const InputDecoration(labelText: 'Nombre')), TextField(controller: phone, decoration: const InputDecoration(labelText: 'Teléfono')), DropdownButtonFormField<String>(initialValue: role, items: const ['ADMIN', 'TECNICO', 'CLIENTE'].map((r) => DropdownMenuItem(value: r, child: Text(r))).toList(), onChanged: (r) => setDialog(() => role = r!), decoration: const InputDecoration(labelText: 'Rol')), SwitchListTile(title: const Text('Activo'), value: active, onChanged: (value) => setDialog(() => active = value))]), actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancelar')), FilledButton(onPressed: () => Navigator.pop(context, name.text.trim().isNotEmpty), child: const Text('Guardar'))])));
    if (ok == true) { try { await Api.updateUser(widget.session.token, item['id'], name.text, role, phone.text, active); refresh(); } catch (e) { message(e); } }
  }
  Future<void> deactivateUser(Map<String, dynamic> item) async {
    final ok = await showDialog<bool>(context: context, builder: (_) => AlertDialog(title: const Text('Desactivar usuario'), content: Text('¿Desactivar "${item['nombre']}"?'), actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancelar')), FilledButton(onPressed: () => Navigator.pop(context, true), child: const Text('Desactivar'))]));
    if (ok == true) { try { await Api.deleteUser(widget.session.token, item['id']); refresh(); } catch (e) { message(e); } }
  }
  @override
  @override
  Widget build(BuildContext context) {
    if (loading) return const Center(child: CircularProgressIndicator());
    return RefreshIndicator(
      onRefresh: refresh,
      child: ListView(
        padding: const EdgeInsets.all(16),
        children: items.map((item) => Card(
          child: ListTile(
            leading: CircleAvatar(child: Text(item['nombre'][0])),
            title: Text(item['nombre']),
            subtitle: Text('${item['email']} · ${item['rol']}'),
            trailing: Row(mainAxisSize: MainAxisSize.min, children: [
              IconButton(tooltip: 'Editar', onPressed: () => edit(item), icon: const Icon(Icons.edit_outlined)),
              IconButton(tooltip: 'Desactivar', onPressed: item['activo'] == true ? () => deactivateUser(item) : null, icon: const Icon(Icons.delete_outline, color: Colors.red)),
            ]),
          ),
        )).toList(),
      ),
    );
  }
}
