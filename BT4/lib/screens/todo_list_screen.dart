import 'package:flutter/material.dart';
import '../models/task.dart';
import '../services/api.dart';
import '../widgets/task_card.dart';
import '../widgets/empty_view.dart';
import 'task_detail_screen.dart';

class TodoListScreen extends StatefulWidget {
  const TodoListScreen({Key? key}) : super(key: key);

  @override
  State<TodoListScreen> createState() => _TodoListScreenState();
}

class _TodoListScreenState extends State<TodoListScreen> {
  List<Task> _tasks = [];
  bool _loading = true;
  bool _refreshing = false;

  @override
  void initState() {
    super.initState();
    _fetchTasks();
  }

  Future<void> _fetchTasks() async {
    try {
      setState(() {
        if (!_refreshing) _loading = true;
      });

      final tasks = await ApiService.getAllTasks();
      
      setState(() {
        _tasks = tasks;
        _loading = false;
        _refreshing = false;
      });
    } catch (e) {
      print('Failed to fetch tasks: $e');
      setState(() {
        _tasks = [];
        _loading = false;
        _refreshing = false;
      });
    }
  }

  Future<void> _onRefresh() async {
    setState(() {
      _refreshing = true;
    });
    await _fetchTasks();
  }

  void _handleTaskPress(Task task) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => TaskDetailScreen(taskId: task.id!),
      ),
    ).then((_) {
      // Refresh list when returning from detail screen
      _fetchTasks();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF5F5F5),
      body: SafeArea(
        child: Column(
          children: [
            // Header
            Container(
              width: double.infinity,
              padding: const EdgeInsets.fromLTRB(20, 50, 20, 20),
              decoration: const BoxDecoration(
                color: Colors.white,
                border: Border(
                  bottom: BorderSide(
                    color: Color(0xFFE0E0E0),
                    width: 1,
                  ),
                ),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'Todo List',
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 10),
                  const Text(
                    'UTH SmartTasks',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.w600,
                      color: Color(0xFF333333),
                    ),
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    'A simple and efficient to-do app',
                    style: TextStyle(
                      fontSize: 14,
                      color: Color(0xFF666666),
                    ),
                  ),
                ],
              ),
            ),

            // Content
            Expanded(
              child: _loading
                  ? const Center(
                      child: CircularProgressIndicator(
                        color: Color(0xFF007AFF),
                      ),
                    )
                  : _tasks.isEmpty
                      ? const EmptyView()
                      : RefreshIndicator(
                          onRefresh: _onRefresh,
                          child: ListView.builder(
                            padding: const EdgeInsets.all(16),
                            itemCount: _tasks.length,
                            itemBuilder: (context, index) {
                              return TaskCard(
                                task: _tasks[index],
                                onPress: () => _handleTaskPress(_tasks[index]),
                              );
                            },
                          ),
                        ),
            ),
          ],
        ),
      ),
    );
  }
}

