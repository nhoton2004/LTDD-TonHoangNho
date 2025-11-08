class Task {
  final int? id;
  final String? title;
  final String? description;
  final String? status;
  final String? category;
  final String? priority;
  final String? createdAt;
  final List<String>? subtasks;
  final List<String>? attachments;
  final bool? completed;

  Task({
    this.id,
    this.title,
    this.description,
    this.status,
    this.category,
    this.priority,
    this.createdAt,
    this.subtasks,
    this.attachments,
    this.completed,
  });

  factory Task.fromJson(Map<String, dynamic> json) {
    return Task(
      id: json['id'],
      title: json['title'],
      description: json['description'],
      status: json['status'],
      category: json['category'],
      priority: json['priority'],
      createdAt: json['createdAt'] ?? json['created_at'],
      subtasks: json['subtasks'] != null
          ? List<String>.from(json['subtasks'])
          : null,
      attachments: json['attachments'] != null
          ? List<String>.from(json['attachments'])
          : null,
      completed: json['completed'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'status': status,
      'category': category,
      'priority': priority,
      'createdAt': createdAt,
      'subtasks': subtasks,
      'attachments': attachments,
      'completed': completed,
    };
  }
}

