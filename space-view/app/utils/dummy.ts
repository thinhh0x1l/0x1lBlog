import type {
  User, Category, Hashtag, Blog, BlogHashtag, Comment, BlogReaction,
  Bookmark, Follow, Notification, Series, Badge, UserBadge, DailyCheckin,
  UserExpLog, Session, Share, Mention, CommentReaction, SeriesBlog,
  SeriesSubscriber, Status, Story, Canvas, CanvasStroke, Playlist,
  PlaylistSong, MusicComment, MusicReaction, Quest, UserQuest,
  BlindChallenge, BlindChallengeGuess, SkillTree, UserSkillProgress,
  UserSkillUnlock, ProfileView, ReputationHistory,
} from '../../shared'

const random = (min: number, max: number) => Math.floor(Math.random() * (max - min + 1)) + min
const pick = <T>(arr: T[]): T => arr[Math.floor(Math.random() * arr.length)]
const pickN = <T>(arr: T[], n: number): T[] => {
  const shuffled = [...arr].sort(() => 0.5 - Math.random())
  return shuffled.slice(0, n)
}
const slugify = (text: string) => text.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/đ/g, 'd').replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '')
const ulid = () => '01H' + Array.from({length: 24}, () => '0123456789ABCDEFGHJKLMNPQRSTUVWXYZ'[random(0,34)]).join('')

// ===== 1. USERS (100 records) =====
const VietnameseNames = ['Nguyễn Văn An', 'Trần Thị Bình', 'Lê Hoàng Nam', 'Phạm Minh Châu', 'Hoàng Văn Đức', 'Vũ Thị Mai', 'Đặng Văn Hùng', 'Bùi Thị Lan', 'Ngô Văn Phúc', 'Dương Thị Hằng', 'Đỗ Văn Tùng', 'Lý Thị Ngọc', 'Mai Văn Tuấn', 'Hà Thị Thanh', 'Trịnh Văn Kiên', 'Châu Thị Bích', 'Tô Văn Long', 'Phan Thị Hương', 'Đinh Văn Quý', 'Lưu Thị Vy']
const avatars = ['https://i.pravatar.cc/150?u=', 'https://api.dicebear.com/7.x/avataaars/svg?seed=']

export const users: User[] = Array.from({length: 100}, (_, i) => ({
  id: i + 1,
  username: `user_${i + 1}`,
  email: `user${i + 1}@example.com`,
  passwordHash: '$2a$10$hashedpassword',
  displayName: i < 20 ? VietnameseNames[i] : `User ${i + 1}`,
  avatarUrl: `${pick(avatars)}user${i + 1}`,
  bio: i % 3 === 0 ? 'Lập trình viên yêu thích công nghệ' : i % 3 === 1 ? 'Developer & Writer' : '',
  website: i % 5 === 0 ? `https://user${i + 1}.dev` : '',
  location: i % 4 === 0 ? 'Hà Nội' : i % 4 === 1 ? 'TP.HCM' : i % 4 === 2 ? 'Đà Nẵng' : '',
  role: i === 0 ? 'ADMIN' : 'USER',
  isCreator: i < 30,
  status: i === 99 ? 'BANNED' : 'ACTIVE',
  blogCount: random(0, 50),
  followerCount: random(0, 500),
  followingCount: random(0, 200),
  level: random(1, 50),
  exp: random(0, 50000),
  checkinStreak: random(0, 30),
  lastCheckinAt: new Date(Date.now() - random(0, 7) * 86400000).toISOString(),
  balance: random(0, 1000000),
  bonus: random(0, 500000),
  createdAt: new Date(Date.now() - random(30, 365) * 86400000).toISOString(),
  equippedItems: {
    border: i % 3 === 0 ? null : { type: 'GRADIENT', colors: ['#0ea5e9', '#8b5cf6'], rarity: (['COMMON', 'UNCOMMON', 'RARE', 'EPIC'] as const)[i % 4] },
    rolltext: i % 4 === 0 ? null : { text: ['Hello World! Welcome to my blog 🚀', 'Code, Coffee, Repeat ☕', 'Chill vibes only ✨', 'Học hỏi mỗi ngày 📚', 'Full-stack dev journey'][i % 5], speed: (['slow', 'normal', 'fast'] as const)[i % 3] },
  },
  activeMischief: i % 5 === 0 ? { type: 'SLIME', appliedBy: VietnameseNames[(i + 3) % VietnameseNames.length], appliedAt: new Date().toISOString(), expiresAt: new Date(Date.now() + 86400000).toISOString() } : null,
  profileLayout: (['AVATAR_BORDER', 'BIO', 'STATS', 'BLOG_LIST', 'BADGE_WALL', 'STATUSES', 'SKILL_TREE', 'STREAK', 'MUSIC_BOX', 'QUEST'] as const).map((t, i) => ({ widgetType: t, isVisible: [true, true, true, true, true, true, false, false, false, false][i], sortOrder: i })),
  gameMode: false,
}))

// ===== 2. CATEGORIES (15 records) =====
const categoryNames = ['Công nghệ', 'Lập trình', 'Web Development', 'Mobile', 'DevOps', 'AI & ML', 'Database', 'Security', 'Game Dev', 'UI/UX', 'Cloud', 'Blockchain', 'IoT', 'Data Science', 'Career']

export const categories: Category[] = categoryNames.map((name, i) => ({
  id: i + 1,
  name,
  slug: slugify(name),
  description: `Các bài viết về ${name.toLowerCase()}`,
  icon: ['💻', '🔧', '🌐', '📱', '🚀', '🤖', '🗄️', '🔒', '🎮', '🎨', '☁️', '⛓️', '📡', '📊', '💼'][i],
  color: ['#0ea5e9', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899', '#06b6d4', '#f97316', '#84cc16', '#a855f7', '#14b8a6', '#eab308', '#6366f1', '#f43f5e', '#22c55e'][i],
  sortOrder: i,
  blogCount: random(5, 100),
  isVisible: true,
  createdAt: new Date(Date.now() - random(30, 365) * 86400000).toISOString(),
}))

// ===== 3. HASHTAGS (150 records) =====
const hashtagNames = ['javascript', 'vuejs', 'react', 'angular', 'nodejs', 'springboot', 'java', 'python', 'typescript', 'docker', 'kubernetes', 'aws', 'postgresql', 'mongodb', 'redis', 'graphql', 'restapi', 'microservices', 'ci-cd', 'linux', 'git', 'css', 'html', 'tailwindcss', 'svelte', 'nextjs', 'nuxtjs', 'laravel', 'django', 'flask', 'fastapi', 'golang', 'rust', 'swift', 'kotlin', 'flutter', 'react-native', 'electron', 'webpack', 'vite', 'sass', 'bootstrap', 'material-ui', 'chakra-ui', 'vuetify', 'element-plus', 'ant-design', 'primevue', 'alpinejs', 'htmx', 'webassembly', 'deno', 'bun', 'npm', 'yarn', 'pnpm', 'linux', 'nginx', 'apache', 'caddy', 'traefik', 'prometheus', 'grafana', 'elasticsearch', 'kafka', 'rabbitmq', 'minio', 's3', 'cloudflare', 'vercel', 'netlify', 'heroku', 'digitalocean', 'linode', 'vultr', 'firebase', 'supabase', 'planetscale', 'neon', 'turso', 'drizzle', 'prisma', 'typeorm', 'sequelize', 'mybatis', 'hibernate', 'jpa', 'spring-security', 'oauth2', 'jwt', 'bcrypt', 'oauth', 'openid', 'saml', '2fa', 'webauthn', 'passkeys', 'oauth', 'oidc', 'saml', 'ldap', 'rbac', 'abac', 'cors', 'csrf', 'xss', 'ssrf', 'sql-injection', 'owasp', 'pentest', 'bugbounty', 'ctf', 'forensics', 'malware', 'encryption', 'hashing', 'certificate', 'tls', 'ssl', 'vpn', 'tor', 'proxy', 'firewall', 'ids', 'ips', 'siem', 'soc', 'threat-model', 'zero-trust', 'mfa', 'sso', 'iam', 'pam', 'dast', 'sast', ' sca', 'dependency-check', 'sonarqube', 'fortify', 'checkmarx', 'veracode', 'snyk', 'dependabot', 'renovate', 'github-actions', 'gitlab-ci', 'jenkins', 'circleci', 'travis', 'azure-devops', 'bitbucket', 'codebuild', 'codeship', 'drone', 'tekton', 'argo', 'flux', 'helm', 'kustomize', 'istio', 'linkerd', 'envoy', 'consul', 'vault', 'nomad', 'terraform', 'pulumi', 'ansible', 'chef', 'puppet', 'salt', 'cloudformation', 'cdk', 'crossplane']

export const hashtags: Hashtag[] = hashtagNames.map((name, i) => ({
  id: i + 1,
  name,
  usageCount: random(5, 500),
  createdAt: new Date(Date.now() - random(30, 365) * 86400000).toISOString(),
}))

// ===== 4. BLOGS (800 records) =====
const blogTitles = [
  'Hướng dẫn Spring Boot từ A-Z cho người mới bắt đầu',
  'Vue 3 Composition API — những điều cần biết',
  'PostgreSQL optimization tips cho production',
  'Docker Compose cho microservices architecture',
  'Redis caching strategies trong Spring Boot',
  'RESTful API design best practices 2024',
  'TypeScript advanced patterns bạn chưa biết',
  'N+1 query problem và cách giải quyết',
  'CI/CD pipeline với GitHub Actions',
  'Kubernetes cho người mới bắt đầu',
  'GraphQL vs REST — khi nào dùng cái nào?',
  'Linux command line tips cho developer',
  'Git workflow hiệu quả cho team',
  'CSS Grid vs Flexbox — khi nào dùng?',
  'Tailwind CSS — utility-first framework',
  'Node.js performance optimization',
  'Python async/await explained',
  'Go concurrency patterns',
  'Rust ownership và borrowing',
  'Swift UI cho iOS development',
  'Flutter vs React Native — so sánh 2024',
  'AWS Lambda serverless architecture',
  'Microservices vs Monolith — trade-offs',
  'JWT authentication implementation',
  'OAuth2 flow explained simply',
  'Database indexing strategies',
  'SQL query optimization tricks',
  'MongoDB vs PostgreSQL — khi nào dùng?',
  'Caching strategy cho web application',
  'Rate limiting implementation',
  'WebSocket real-time communication',
  'Message queue với RabbitMQ',
  'Elasticsearch full-text search',
  'Nginx reverse proxy configuration',
  'Prometheus monitoring setup',
  'Grafana dashboard creation',
  'Log aggregation với ELK stack',
  'Security headers cho web application',
  'XSS prevention techniques',
  'CSRF protection implementation',
  'Content Security Policy explained',
  'Web performance optimization checklist',
  'Core Web Vitals improvement tips',
  'Progressive Web App development',
  'Service Worker caching strategies',
  'WebAssembly overview và use cases',
  'Deno vs Node.js comparison',
  'Bun runtime — fast JavaScript',
  'Package managers: npm vs yarn vs pnpm',
  'Code review best practices',
  'Refactoring techniques cho legacy code',
  'Test-driven development workflow',
  'Unit testing với Jest',
  'Integration testing strategies',
  'E2E testing với Playwright',
  'Load testing với k6',
  'Chaos engineering basics',
  'Observability trong distributed systems',
  'Tracing với OpenTelemetry',
  'Distributed tracing explained',
  'Circuit breaker pattern implementation',
  'Retry pattern với exponential backoff',
  'Bulkhead pattern cho microservices',
  'Saga pattern cho distributed transactions',
  'CQRS và Event Sourcing',
  'Domain-Driven Design overview',
  'Hexagonal Architecture explained',
  'Clean Architecture implementation',
  'SOLID principles practical examples',
  'Design patterns trong Java',
  'Design patterns trong JavaScript',
  'Functional programming concepts',
  'Reactive programming với RxJS',
  'State management Vue 3 — Pinia vs Vuex',
  'State management React — Redux vs Zustand',
  'Component design patterns',
  'API versioning strategies',
  'Pagination techniques — offset vs cursor',
  'Infinite scroll implementation',
  'Lazy loading images optimization',
  'Virtual scrolling cho danh sách lớn',
  'Web font optimization',
  'Image optimization cho web',
  'CDN strategy cho web application',
  'SSL/TLS certificate setup',
  'Domain configuration và DNS',
  'Email sending service implementation',
  'File upload với drag & drop',
  'Image processing pipeline',
  'Video streaming architecture',
  'Payment gateway integration',
  'E-commerce system design',
  'Social media platform architecture',
  'Blog platform architecture',
  'CMS system design',
  'Real-time chat system design',
  'Notification system architecture',
  'Search engine implementation',
  'Recommendation system basics',
  'Machine learning cho beginners',
  'Neural network explained simply',
  'Natural Language Processing overview',
  'Computer vision basics',
  'Data pipeline design',
  'ETL process implementation',
  'Data warehousing concepts',
  'Business intelligence tools',
  'Dashboard design principles',
  'Data visualization best practices',
  'Chart.js vs D3.js comparison',
  'SVG animation techniques',
  'Canvas API cho game development',
  'Three.js 3D rendering overview',
  'WebGL programming basics',
  'AR/VR web development',
  'Blockchain development introduction',
  'Smart contract với Solidity',
  'DeFi protocol design',
  'NFT marketplace development',
  'Web3 wallet integration',
  'IoT protocols overview',
  'MQTT broker setup',
  'Edge computing concepts',
  '5G network architecture',
  'DevOps culture và practices',
  'Infrastructure as Code',
  'Configuration management',
  'Secret management best practices',
  'Disaster recovery planning',
  'Backup strategies cho database',
  'High availability architecture',
  'Load balancing strategies',
  'Auto-scaling configuration',
  'Cost optimization cloud',
  'Green computing practices',
  'Sustainable software engineering',
  'Accessibility (a11y) guidelines',
  'SEO optimization techniques',
  'Content strategy cho blog',
  'Technical writing tips',
  'Open source contribution guide',
  'Developer portfolio tips',
  'Interview preparation guide',
  'Salary negotiation tips',
  'Remote work best practices',
  'Time management cho developer',
  'Burnout prevention strategies',
  'Learning path cho web developer',
  'Career growth trong tech industry',
  'Building personal brand',
  'Networking tips cho developer',
  'Conference speaking preparation',
  'Writing technical articles',
  'Code kata practices',
  'Algorithm study guide',
  'Data structures explained',
  'System design interview',
  'Behavioral interview tips',
  'Portfolio project ideas',
  'Side project monetization',
  'Freelancing tips cho developer',
  'Startup technical co-founder guide',
  'Team leadership trong tech',
  'Agile vs Scrum vs Kanban',
  'Product management cho developer',
  'UX research methods',
  'Design thinking process',
  'A/B testing implementation',
  'Analytics integration guide',
  'Error tracking với Sentry',
  'APM tools comparison',
  'Database migration strategies',
  'Schema evolution techniques',
  'Data modeling best practices',
  'Normalization vs denormalization',
  'ACID properties explained',
  'CAP theorem practical implications',
  'Database replication strategies',
  'Sharding techniques',
  'Connection pooling optimization',
  'Query plan analysis',
  'Index usage optimization',
  'Partition pruning strategies',
  'Materialized views usage',
  'Stored procedures vs application logic',
  'Database security hardening',
  'Encryption at rest và in transit',
  'Audit logging implementation',
  'Compliance requirements (GDPR, SOC2)',
  'Penetration testing basics',
  'Vulnerability assessment',
  'Security code review checklist',
  'Incident response planning',
  'Post-mortem culture',
  'SLA và SLO definitions',
  'Error budget management',
  'Feature flags implementation',
  'Canary deployment strategy',
  'Blue-green deployment',
  'Rolling update strategy',
  'Database backup restoration',
  'Chaos engineering practices',
  'Game day exercises',
  'Tabletop exercises cho incident',
]

const blogContents: string[] = [
  `# Hướng dẫn Spring Boot từ A-Z

## Giới thiệu

Spring Boot là framework phổ biến nhất để phát triển ứng dụng Java backend. Trong bài viết này, chúng ta sẽ tìm hiểu từ cơ bản đến nâng cao.

## Tại sao chọn Spring Boot?

### Ưu điểm

- **Tốc độ phát triển nhanh**: Auto-configuration giúp setup dự án trong vài phút
- **Ecosystem phong phú**: Spring Security, Spring Data, Spring Cloud...
- **Community lớn**: Tài liệu phong phú, nhiều giải pháp cho mọi vấn đề

### So sánh với các framework khác

| Framework | Ngôn ngữ | Ưu điểm |
|-----------|----------|----------|
| Spring Boot | Java | Enterprise, ecosystem |
| Node.js | JavaScript | Non-blocking, realtime |
| Django | Python | Nhanh, batteries-included |

## Setup environment

### Yêu cầu

- JDK 17+
- Maven hoặc Gradle
- IDE: IntelliJ IDEA hoặc VS Code

### Tạo project

\`\`\`bash
curl https://start.spring.io/starter.zip -d type=maven-project -d dependencies=web,data-jpa,postgresql -o my-app.zip
\`\`\`

### Cấu trúc thư mục

\`\`\`
src/main/java/
├── controller/     # REST API endpoints
├── service/        # Business logic
├── repository/     # Data access
├── model/          # Entity classes
└── config/         # Configuration
\`\`\`

## REST API Design

### HTTP Methods

- **GET**: Lấy dữ liệu
- **POST**: Tạo mới
- **PUT**: Cập nhật toàn bộ
- **PATCH**: Cập nhật một phần
- **DELETE**: Xóa dữ liệu

### Status Codes

\`\`\`
200 OK              → Thành công
201 Created         → Tạo mới thành công
400 Bad Request     → Dữ liệu đầu vào sai
401 Unauthorized    → Chưa xác thực
403 Forbidden       → Không có quyền
404 Not Found       → Không tìm thấy
500 Server Error    → Lỗi server
\`\`\`

## Database Integration

### JPA Repository

\`\`\`java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByStatus(String status);
}
\`\`\`

### Query Methods

Spring Data JPA hỗ trợ query tự động từ method name:

- findByEmail(String email) → WHERE email = ?
- findByStatusAndAge(String status, Integer age) → WHERE status = ? AND age = ?
- findByNameContaining(String name) → WHERE name LIKE '%?%'

## Security

### JWT Authentication

\`\`\`java
@Component
public class JwtTokenProvider {
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }
}
\`\`\`

### Spring Security Config

\`\`\`java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
\`\`\`

## Testing

### Unit Test

\`\`\`java
@SpringBootTest
class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Test
    void shouldCreateUser() {
        // Given
        User user = new User("test@example.com", "Test User");
        when(userRepository.save(any())).thenReturn(user);
        
        // When
        User created = userService.createUser(user);
        
        // Then
        assertThat(created.getEmail()).isEqualTo("test@example.com");
    }
}
\`\`\`

## Deployment

### Docker

\`\`\`dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
\`\`\`

### Production Checklist

- [ ] Cấu hình logging đúng mức
- [ ] Setup monitoring (Prometheus + Grafana)
- [ ] Configure rate limiting
- [ ] Enable HTTPS
- [ ] Setup backup strategy

## Kết luận

Spring Boot là lựa chọn tuyệt vời cho dự án Java backend. Với ecosystem phong phú và community lớn, bạn sẽ luôn tìm được giải pháp cho mọi vấn đề.

**Đọc thêm:**
- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Baeldung - Spring Boot Tutorials](https://www.baeldung.com/spring-boot)`,

  `# Vue 3 Composition API — Complete Guide

## Tổng quan

Vue 3引入了 Composition API, một cách mới để tổ chức code trong Vue components.

## Tại sao cần Composition API?

### Vấn đề với Options API

Khi component phức tạp, code liên quan đến cùng một feature bị分散 ra nhiều nơi:

\`\`\`javascript
// Options API — code phân mảnh
export default {
  data() {
    return { count: 0, loading: false, error: null }
  },
  methods: {
    async fetchData() { ... },
    handleClick() { ... }
  },
  mounted() {
    this.fetchData()
  }
}
\`\`\`

### Giải pháp với Composition API

\`\`\`javascript
// Composition API — code tập trung
import { ref, onMounted } from 'vue'

const count = ref(0)
const loading = ref(false)

async function fetchData() { ... }
function handleClick() { ... }

onMounted(() => fetchData())
\`\`\`

## Core Concepts

### Ref vs Reactive

\`\`\`javascript
// Ref — primitives
const count = ref(0)
console.log(count.value) // 0

// Reactive — objects
const state = reactive({ count: 0, name: 'Vue' })
console.log(state.count) // 0
\`\`\`

### Computed Properties

\`\`\`javascript
const firstName = ref('John')
const lastName = ref('Doe')

const fullName = computed(() => {
  return \`\${firstName.value} \${lastName.value}\`
})
\`\`\`

### Watch & WatchEffect

\`\`\`javascript
// Watch — theo dõi specific ref
watch(count, (newVal, oldVal) => {
  console.log(\`Count changed: \${oldVal} → \${newVal}\`)
})

// WatchEffect — tự động theo dõi dependencies
watchEffect(() => {
  console.log(\`Count is: \${count.value}\`)
})
\`\`\`

## Composables

### Tạo composable

\`\`\`javascript
// useCounter.js
import { ref } from 'vue'

export function useCounter(initialValue = 0) {
  const count = ref(initialValue)
  
  function increment() { count.value++ }
  function decrement() { count.value-- }
  function reset() { count.value = initialValue }
  
  return { count, increment, decrement, reset }
}
\`\`\`

### Sử dụng

\`\`\`javascript
// Component.vue
import { useCounter } from '@/composables/useCounter'

const { count, increment, decrement, reset } = useCounter(10)
\`\`\`

## Lifecycle Hooks

\`\`\`javascript
import { 
  onBeforeMount, onMounted, 
  onBeforeUpdate, onUpdated,
  onBeforeUnmount, onUnmounted 
} from 'vue'

onMounted(() => {
  console.log('Component mounted')
})

onUnmounted(() => {
  console.log('Component unmounted')
})
\`\`\`

## Best Practices

1. **Sử dụng script setup** — syntax ngắn gọn nhất
2. **Tách logic thành composables** — tái sử dụng code
3. **Tránh reactive primitive** — dùng ref cho primitives
4. **Computed cho derived state** — không computed cho side effects
5. **Watch có targeted** — chỉ watch khi cần

## Kết luận

Composition API giúp code Vue 3 organized hơn, dễ maintain hơn, và dễ test hơn. Đây là tương lai của Vue development.`,

  `# PostgreSQL Optimization Tips cho Production

## Tổng quan

PostgreSQL là hệ quản trị database mã nguồn mở mạnh mẽ nhất hiện nay. Trong bài viết này, chúng ta sẽ tìm hiểu các tips tối ưu hóa cho production.

## Index Optimization

### Composite Index

\`\`\`sql
-- Tạo composite index
CREATE INDEX idx_blogs_author_status 
ON blogs(author_id, status, published_at DESC);

-- Query sử dụng index
SELECT * FROM blogs 
WHERE author_id = 1 AND status = 'PUBLISHED'
ORDER BY published_at DESC;
\`\`\`

### Partial Index

\`\`\`sql
-- Chỉ index rows active
CREATE INDEX idx_users_active 
ON users(email) 
WHERE status = 'ACTIVE' AND deleted_at IS NULL;
\`\`\`

### Covering Index

\`\`\`sql
-- Index chứa tất cả columns cần query
CREATE INDEX idx_blogs_covering 
ON blogs(author_id, published_at DESC) 
INCLUDE (title, slug, views);
\`\`\`

## Query Optimization

### Sử dụng EXPLAIN ANALYZE

\`\`\`sql
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM blogs WHERE author_id = 1;
\`\`\`

### Tránh N+1 Queries

\`\`\`sql
-- SAI: N+1 queries
SELECT * FROM blogs;
-- Mỗi blog → query author riêng

-- ĐÚNG: JOIN trong 1 query
SELECT b.*, u.display_name 
FROM blogs b 
JOIN users u ON b.author_id = u.id;
\`\`\`

### Batch Operations

\`\`\`sql
-- INSERT batch
INSERT INTO blog_tags (blog_id, tag_id) VALUES 
(1, 1), (1, 2), (1, 3), (2, 1);

-- UPDATE batch
UPDATE blogs SET views = views + 1 
WHERE id IN (1, 2, 3, 4, 5);
\`\`\`

## Connection Pooling

### HikariCP Configuration

\`\`\`yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      max-lifetime: 1800000
      connection-timeout: 20000
\`\`\`

## Monitoring

### pg_stat_statements

\`\`\`sql
-- Top 10 queries chạy chậm nhất
SELECT query, calls, mean_exec_time, total_exec_time
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;
\`\`\`

### Index Usage

\`\`\`sql
-- Index không được sử dụng
SELECT indexrelname, idx_scan 
FROM pg_stat_user_indexes 
WHERE idx_scan = 0;
\`\`\`

## Best Practices

1. **Regular VACUUM ANALYZE** — Dọn dẹp dead tuples
2. **Partitioning** — Chia bảng lớn theo thời gian
3. **Read Replicas** — Tách read/write traffic
4. **Connection pooling** — HikariCP hoặc PgBouncer
5. **Monitoring** — pg_stat_statements + Prometheus

## Kết luận

Tối ưu PostgreSQL là quá trình liên tục. Bắt đầu từ việc hiểu query plans, sau đó tối ưu index và connection pooling.`,
]

const richBlogContent = `# Hướng dẫn Spring Boot từ A-Z

## Giới thiệu

Spring Boot là framework phổ biến nhất để phát triển ứng dụng Java backend. Trong bài viết này, chúng ta sẽ tìm hiểu từ cơ bản đến nâng cao.

## Tại sao chọn Spring Boot?

### Ưu điểm

- **Tốc độ phát triển nhanh**: Auto-configuration giúp setup dự án trong vài phút
- **Ecosystem phong phú**: Spring Security, Spring Data, Spring Cloud...
- **Community lớn**: Tài liệu phong phú, nhiều giải pháp cho mọi vấn đề

### So sánh với các framework khác

| Framework | Ngôn ngữ | Ưu điểm |
|-----------|----------|----------|
| Spring Boot | Java | Enterprise, ecosystem |
| Node.js | JavaScript | Non-blocking, realtime |
| Django | Python | Nhanh, batteries-included |

## Setup environment

### Yêu cầu

- JDK 17+
- Maven hoặc Gradle
- IDE: IntelliJ IDEA hoặc VS Code

### Tạo project

\`\`\`bash
curl https://start.spring.io/starter.zip -d type=maven-project -d dependencies=web,data-jpa,postgresql -o my-app.zip
\`\`\`

### Cấu trúc thư mục

\`\`\`
src/main/java/
├── controller/     # REST API endpoints
├── service/        # Business logic
├── repository/     # Data access
├── model/          # Entity classes
└── config/         # Configuration
\`\`\`

## REST API Design

### HTTP Methods

- **GET**: Lấy dữ liệu
- **POST**: Tạo mới
- **PUT**: Cập nhật toàn bộ
- **PATCH**: Cập nhật một phần
- **DELETE**: Xóa dữ liệu

### Status Codes

\`\`\`
200 OK              → Thành công
201 Created         → Tạo mới thành công
400 Bad Request     → Dữ liệu đầu vào sai
401 Unauthorized    → Chưa xác thực
403 Forbidden       → Không có quyền
404 Not Found       → Không tìm thấy
500 Server Error    → Lỗi server
\`\`\`

## Database Integration

### JPA Repository

\`\`\`java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByStatus(String status);
}
\`\`\`

### Query Methods

Spring Data JPA hỗ trợ query tự động từ method name:

- findByEmail(String email) → WHERE email = ?
- findByStatusAndAge(String status, Integer age) → WHERE status = ? AND age = ?
- findByNameContaining(String name) → WHERE name LIKE '%?%'

## Security

### JWT Authentication

\`\`\`java
@Component
public class JwtTokenProvider {
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }
}
\`\`\`

### Spring Security Config

\`\`\`java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
\`\`\`

## Testing

### Unit Test

\`\`\`java
@SpringBootTest
class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Test
    void shouldCreateUser() {
        User user = new User("test@example.com", "Test User");
        when(userRepository.save(any())).thenReturn(user);
        
        User created = userService.createUser(user);
        
        assertThat(created.getEmail()).isEqualTo("test@example.com");
    }
}
\`\`\`

## Deployment

### Docker

\`\`\`dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
\`\`\`

### Production Checklist

- [ ] Cấu hình logging đúng mức
- [ ] Setup monitoring (Prometheus + Grafana)
- [ ] Configure rate limiting
- [ ] Enable HTTPS
- [ ] Setup backup strategy

## Kết luận

Spring Boot là lựa chọn tuyệt vời cho dự án Java backend. Với ecosystem phong phú và community lớn, bạn sẽ luôn tìm được giải pháp cho mọi vấn đề.

**Đọc thêm:**
- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Baeldung - Spring Boot Tutorials](https://www.baeldung.com/spring-boot)`

export const blogs: Blog[] = Array.from({length: 800}, (_, i) => {
  const authorId = random(1, 100)
  const categoryId = random(1, 15)
  const title = i < blogTitles.length ? blogTitles[i] : `Bài viết #${i + 1} — ${pick(['Hướng dẫn', 'Tìm hiểu', 'So sánh', 'Tối ưu', 'Thực hành', 'Giới thiệu'])} ${pick(hashtagNames.slice(0, 50))}`
  const publishedAt = new Date(Date.now() - random(0, 365) * 86400000)
  return {
    id: i + 1,
    authorId,
    categoryId,
    title,
    slug: slugify(title) + '-' + (i + 1),
    content: i === 0 ? richBlogContent : i < 3 ? blogContents[i % blogContents.length] : `${pick(blogContents)}\n\n${'Nội dung bài viết chi tiết. '.repeat(random(10, 50))}`,
    description: `${pick(['Hướng dẫn', 'Tổng hợp', 'Chia sẻ', 'Phân tích'])} ${title.toLowerCase().slice(0, 80)}`,
    coverImage: i % 3 === 0 ? `https://picsum.photos/seed/blog${i}/800/400` : null,
    contentType: 'MARKDOWN',
    status: i % 20 === 0 ? 'DRAFT' : i % 25 === 0 ? 'ARCHIVED' : 'PUBLISHED',
    visibility: i % 50 === 0 ? 'MEMBERS_ONLY' : i % 100 === 0 ? 'PAID' : 'PUBLIC',
    isTop: i < 5,
    isRecommend: i % 10 === 0,
    allowComments: true,
    words: random(200, 5000),
    readTime: random(3, 30),
    views: random(10, 50000),
    likeCount: random(0, 500),
    loveCount: random(0, 100),
    hahaCount: random(0, 50),
    wowCount: random(0, 30),
    sadCount: random(0, 20),
    angryCount: random(0, 10),
    commentCount: random(0, 200),
    bookmarkCount: random(0, 100),
    shareCount: random(0, 50),
    publishedAt: publishedAt.toISOString(),
    createdAt: publishedAt.toISOString(),
    updatedAt: new Date(publishedAt.getTime() + random(0, 30) * 86400000).toISOString(),
  }
})

// ===== 5. BLOG_HASHTAGS (2000 records) =====
export const blogHashtags: BlogHashtag[] = Array.from({length: 2000}, () => ({
  blogId: random(1, 800),
  hashtagId: random(1, 150),
}))

// ===== 6. COMMENTS (1500 records) =====
export const comments: Comment[] = Array.from({length: 1500}, (_, i) => {
  const isReply = i > 500 && Math.random() > 0.6
  return {
    id: i + 1,
    blogId: random(1, 800),
    parentId: isReply ? random(1, 500) : null,
    userId: Math.random() > 0.3 ? random(1, 100) : null,
    guestName: Math.random() > 0.3 ? null : `Khách ${random(1, 1000)}`,
    content: pick([
      'Bài viết rất hay, cảm ơn tác giả!',
      'Có thể giải thích thêm phần này được không?',
      'Mình đã thử và hoạt động tốt.',
      'Cảm ơn vì đã chia sẻ kinh nghiệm.',
      'Bài viết hữu ích, đã bookmark.',
      'Mình có thắc mắc về phần này...',
      'Great article! Very helpful.',
      'Cần update thêm cho phiên bản mới nhất.',
      'Đã áp dụng vào dự án thực tế, rất hiệu quả.',
      'Tác giả viết rõ ràng, dễ hiểu.',
      'Có video hướng dẫn không?',
      'Phần code example hơi ngắn.',
      'Thanks for sharing!',
      'Rất mong phần tiếp theo.',
      'Mình cũng gặp vấn đề tương tự.',
    ]),
    status: 'APPROVED',
    likeCount: random(0, 50),
    replyCount: isReply ? 0 : random(0, 10),
    createdAt: new Date(Date.now() - random(0, 180) * 86400000).toISOString(),
    updatedAt: new Date(Date.now() - random(0, 180) * 86400000).toISOString(),
  }
})

// ===== 7. BLOG_REACTIONS (3000 records) =====
const reactionTypes = ['LIKE', 'LOVE', 'HAHA', 'WOW', 'SAD', 'ANGRY'] as const
export const blogReactions: BlogReaction[] = Array.from({length: 3000}, () => ({
  id: random(1, 100000),
  userId: random(1, 100),
  blogId: random(1, 800),
  type: pick(reactionTypes as unknown as BlogReaction['type'][]),
  createdAt: new Date(Date.now() - random(0, 180) * 86400000).toISOString(),
}))

// ===== 8. BOOKMARKS (800 records) =====
export const bookmarks: Bookmark[] = Array.from({length: 800}, () => ({
  id: random(1, 100000),
  userId: random(1, 100),
  blogId: random(1, 800),
  collection: pick(['Đọc sau', 'Học tập', 'Tham khảo', 'Yêu thích', null] as (string | null)[]),
  note: Math.random() > 0.7 ? 'Bài viết hữu ích' : null,
  isPublic: Math.random() > 0.8,
  createdAt: new Date(Date.now() - random(0, 180) * 86400000).toISOString(),
}))

// ===== 9. FOLLOWS (1500 records) =====
export const follows: Follow[] = Array.from({length: 1500}, () => {
  const followerId = random(1, 100)
  let followingId = random(1, 100)
  while (followingId === followerId) followingId = random(1, 100)
  return {
    id: random(1, 100000),
    followerId,
    followingId,
    createdAt: new Date(Date.now() - random(0, 365) * 86400000).toISOString(),
  }
})

// ===== 10. NOTIFICATIONS (1000 records) =====
const notifTypes = ['NEW_COMMENT', 'NEW_REPLY', 'NEW_FOLLOWER', 'NEW_BLOG', 'LIKE_BLOG', 'LIKE_COMMENT', 'BADGE_AWARD', 'SERIES_NEW_POST', 'MENTION'] as const
export const notifications: Notification[] = Array.from({length: 1000}, () => ({
  id: random(1, 100000),
  userId: random(1, 100),
  actorId: random(1, 100),
  type: pick(notifTypes as unknown as string[]),
  title: pick(['Bình luận mới', 'Lượt thích mới', 'Người theo dõi mới', 'Bài viết mới', 'Huy hiệu mới']),
  message: pick(['đã bình luận bài viết của bạn', 'đã thích bài viết của bạn', 'đã theo dõi bạn', 'đã đăng bài viết mới', 'đã trao cho bạn huy hiệu']),
  targetType: pick(['blog', 'comment', 'user', null] as (string | null)[]),
  targetId: random(1, 800),
  isRead: Math.random() > 0.4,
  createdAt: new Date(Date.now() - random(0, 90) * 86400000).toISOString(),
}))

// ===== 11. BLOG_SERIES (30 records) =====
export const blogSeries: Series[] = Array.from({length: 30}, (_, i) => ({
  id: i + 1,
  name: pick([
    'Học Spring Boot từ cơ bản đến nâng cao',
    'Vue 3 Complete Guide',
    'Microservices Architecture Series',
    'DevOps Fundamentals',
    'Python for Data Science',
    'System Design Interview Prep',
    'JavaScript Advanced Patterns',
    'Database Optimization Series',
    'Security Best Practices',
    'Cloud Native Development',
  ]),
  description: `Series bài viết về ${pick(['Spring Boot', 'Vue.js', 'Microservices', 'DevOps', 'Python', 'System Design'])}`,
  coverImage: `https://picsum.photos/seed/series${i}/400/200`,
  authorId: random(1, 30),
  status: pick(['ACTIVE', 'COMPLETED', 'ARCHIVED'] as const),
  price: i % 5 === 0 ? random(50000, 200000) : null,
  postCount: random(3, 20),
  subscriberCount: random(10, 500),
  createdAt: new Date(Date.now() - random(30, 365) * 86400000).toISOString(),
  updatedAt: new Date(Date.now() - random(0, 30) * 86400000).toISOString(),
}))

// ===== 12. BADGES (25 records) =====
export const badges: Badge[] = Array.from({length: 25}, (_, i) => ({
  id: i + 1,
  name: ['first-blog', 'popular-author', 'commenter', 'bookworm', 'social-butterfly', 'early-adopter', 'streak-master', 'helpful-reviewer', 'code-ninja', 'guru', 'mentor', 'explorer', 'achiever', 'collector', 'pioneer', 'champion', 'legend', 'elite', 'master', 'virtuoso', 'sage', 'oracle', 'titan', 'phoenix', 'diamond'][i],
  displayName: ['Bài viết đầu tiên', 'Tác giả phổ biến', 'Bình luận viên', 'Đọc giả', 'Mạng xã hội', 'Người tiên phong', 'Điểm danh liên tục', 'Reviewer hữu ích', 'Code Ninja', 'Guru', 'Mentor', 'Explorer', 'Achiever', 'Collector', 'Pioneer', 'Champion', 'Legend', 'Elite', 'Master', 'Virtuoso', 'Sage', 'Oracle', 'Titan', 'Phoenix', 'Diamond'][i],
  description: `Huy hiệu cho thành tích ${['viết bài', 'được yêu thích', 'bình luận', 'đọc bài', 'tương tác', 'sớm nhất', 'điểm danh', 'review', 'code', 'chia sẻ'][i % 10]}`,
  icon: ['✍️', '⭐', '💬', '📖', '🤝', '🚀', '🔥', '👍', '💎', '🧠', '🎓', '🔭', '🏆', '📦', '🌱', '🏅', '👑', '💎', '🎯', '🎵', '📜', '🔮', '⛰️', '🔥', '💎'][i],
  tier: (['BRONZE', 'BRONZE', 'BRONZE', 'SILVER', 'SILVER', 'SILVER', 'GOLD', 'GOLD', 'GOLD', 'BRONZE', 'SILVER', 'GOLD', 'BRONZE', 'SILVER', 'GOLD', 'GOLD', 'GOLD', 'GOLD', 'GOLD', 'GOLD', 'GOLD', 'GOLD', 'GOLD', 'GOLD', 'GOLD'] as const)[i],
  criteria: JSON.stringify({ type: ['blog_count', 'follower_count', 'comment_count', 'read_count'][i % 4], threshold: [1, 100, 50, 1000][i % 4] }),
  createdAt: new Date(Date.now() - random(30, 365) * 86400000).toISOString(),
}))

// ===== 13. USER_BADGES (200 records) =====
export const userBadges: UserBadge[] = Array.from({length: 200}, () => ({
  id: random(1, 100000),
  userId: random(1, 100),
  badgeId: random(1, 25),
  awardedAt: new Date(Date.now() - random(0, 365) * 86400000).toISOString(),
}))

// ===== 14. DAILY_CHECKINS (800 records) =====
export const dailyCheckins: DailyCheckin[] = Array.from({length: 800}, () => ({
  id: random(1, 100000),
  userId: random(1, 100),
  checkinDate: new Date(Date.now() - random(0, 90) * 86400000).toISOString().split('T')[0],
  streakAtTime: random(1, 30),
  bonusExp: random(0, 100),
  createdAt: new Date(Date.now() - random(0, 90) * 86400000).toISOString(),
}))

// ===== 15. USER_EXP_LOG (500 records) =====
const expReasons = ['BLOG_CREATED', 'COMMENT', 'LIKE_RECEIVED', 'DAILY_CHECKIN', 'BADGE_AWARD', 'FOLLOW_RECEIVED'] as const
export const userExpLog: UserExpLog[] = Array.from({length: 500}, () => ({
  id: random(1, 100000),
  userId: random(1, 100),
  amount: pick([10, 20, 50, 100, 200]),
  reason: pick(expReasons as unknown as string[]),
  refId: random(1, 800),
  createdAt: new Date(Date.now() - random(0, 365) * 86400000).toISOString(),
}))

// ===== 16. SESSIONS (150 records) =====
const deviceTypes = ['DESKTOP', 'MOBILE', 'TABLET'] as const
const countries = ['VN', 'US', 'JP', 'KR', 'TH', 'SG'] as const
export const sessions: Session[] = Array.from({length: 150}, () => ({
  id: random(1, 100000),
  sessionId: ulid(),
  userId: Math.random() > 0.3 ? random(1, 100) : null,
  ipAddress: `${random(1, 255)}.${random(0, 255)}.${random(0, 255)}.${random(0, 255)}`,
  userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
  deviceType: pick(deviceTypes as unknown as Session['deviceType'][]),
  countryCode: pick(countries as unknown as string[]),
  city: pick(['Hà Nội', 'TP.HCM', 'Đà Nẵng', 'Hải Phòng', 'Seattle', 'Tokyo']),
  startedAt: new Date(Date.now() - random(0, 30) * 86400000).toISOString(),
  endedAt: Math.random() > 0.3 ? new Date(Date.now() - random(0, 30) * 86400000).toISOString() : null,
  durationSeconds: random(60, 3600),
}))

// ===== 17. SHARES (300 records) =====
export const shares: Share[] = Array.from({length: 300}, () => ({
  id: random(1, 100000),
  blogId: random(1, 800),
  userId: random(1, 100),
  quoteText: Math.random() > 0.7 ? 'Bài viết rất hay!' : null,
  platform: pick(['INTERNAL', 'FACEBOOK', 'TWITTER', 'LINKEDIN'] as const),
  createdAt: new Date(Date.now() - random(0, 180) * 86400000).toISOString(),
}))

// ===== 18. MENTIONS (100 records) =====
export const mentions: Mention[] = Array.from({length: 100}, () => ({
  id: random(1, 100000),
  targetUserId: random(1, 100),
  mentionedBy: random(1, 100),
  sourceType: pick(['BLOG', 'COMMENT'] as const),
  sourceId: random(1, 800),
  createdAt: new Date(Date.now() - random(0, 180) * 86400000).toISOString(),
}))

// ===== 19. COMMENT_REACTIONS (500 records) =====
export const commentReactions: CommentReaction[] = Array.from({length: 500}, () => ({
  id: random(1, 100000),
  userId: random(1, 100),
  commentId: random(1, 1500),
  createdAt: new Date(Date.now() - random(0, 180) * 86400000).toISOString(),
}))

// ===== 20. SERIES_BLOGS (100 records) =====
export const seriesBlogs: SeriesBlog[] = Array.from({length: 100}, () => ({
  seriesId: random(1, 30),
  blogId: random(1, 800),
  sortOrder: random(0, 20),
  note: null,
  createdAt: new Date(Date.now() - random(0, 365) * 86400000).toISOString(),
}))

// ===== 21. SERIES_SUBSCRIBERS (200 records) =====
export const seriesSubscribers: SeriesSubscriber[] = Array.from({length: 200}, () => ({
  id: random(1, 100000),
  seriesId: random(1, 30),
  userId: random(1, 100),
  createdAt: new Date(Date.now() - random(0, 365) * 86400000).toISOString(),
}))

// ─── R101: Statuses ───────────────────────────────────────────────
export const statuses: Status[] = Array.from({length: 200}, (_, i) => ({
  id: i + 1,
  userId: users[i % 100].id,
  content: ['Đang học Rust...', 'Hôm nay trời đẹp quá!', 'Vừa release feature mới 🚀', 'Có ai rảnh review PR giúp mình không?', 'Đang đọc sách "Clean Code"'][i % 5],
  type: (['TEXT', 'POLL', 'THREAD'] as const)[i % 3],
  pollOptions: i % 3 === 1 ? JSON.stringify([
    {id: 1, text: 'Rust', votes: 12},
    {id: 2, text: 'Go', votes: 8},
    {id: 3, text: 'TypeScript', votes: 5},
  ]) : null,
  totalVotes: i % 3 === 1 ? 25 : 0,
  visibility: 'PUBLIC',
  createdAt: new Date(Date.now() - i * 3600000).toISOString(),
  updatedAt: new Date(Date.now() - i * 3600000).toISOString(),
}))

// ─── R301: Stories ────────────────────────────────────────────────
const storyCaptions = [
  'Đang code đây...', 'Cà phê sáng ☕', 'Bug mới, ngày mới!', 'Mặt mộc đi làm',
  'Chill cuối tuần', 'Mới ra mắt tính năng 🚀', 'Ăn trưa cùng team', 'Đi dạo ngắm cảnh',
  'Tản bộ sáng sớm', 'Laptop mới kìa!', 'Sương mù Hà Nội', 'Hoàng hôn đẹp quá',
  'Đọc sách bên cafe', 'Giao diện mới đây', 'Đang review code', 'Meetup công nghệ',
  'Mèo ú đáng yêu', 'Tập gym sáng', 'Mưa rồi ☔', 'Nấu ăn tối nay',
  'Chuẩn bị đi chơi', 'Xem phim hay quá', 'Vẽ vời tí', 'Chụp góc nào cũng đẹp',
  'Cảm giác bình yên', 'Tản mạn chiều thứ 7', 'Viết blog mới', 'Mãi mới xong task',
  'Công viên cuối tuần', 'Cùng nhau code nào', 'Trà đá vỉa hè', 'Ốm mất rồi 😷',
  'Demo sản phẩm mới', 'Team building vui vẻ', 'Đi phượt cuối tuần', 'Mây mù trên đỉnh núi',
  'Biển xanh cát trắng', 'Món ngon mới thử', 'Workshop hôm nay', 'Hackathon đêm qua',
  'Chụp cùng team dev', 'Đèn led bàn phím mới', 'Setup bàn làm việc', 'Sách mới mua 📚',
  'Chạy bộ sáng sớm', 'Phố đi bộ cuối tuần', 'Thử nghiệm tính năng', 'Review pull request',
  'Đang deploy lên prod', 'Hot fix đêm khuya 😅', 'Bàn phím cơ mới', 'Gym ngày chân 💪',
  'Sinh nhật team 🎂', 'Chụp ảnh sản phẩm', 'UI/UX mới lên ý tưởng', 'Phỏng vấn ứng viên',
  'Coding session cùng nhau', 'Đám mây trên bầu trời', 'Thức đêm code game', 'Bữa sáng healthy',
  'Đi siêu thị mua sắm', 'Góc làm việc tối giản', 'Cây cảnh để bàn', 'Đèn ngủ ấm áp',
  'Tuyết rơi ở Sa Pa', 'Check-in văn phòng mới', 'Họp team hàng tuần', 'Ăn vặt cùng đồng nghiệp',
  'Đang học tiếng Nhật', 'Build thất bại lần N', 'Chạy unit test cả đêm', 'Đi cà phê coding',
  'Sống ảo bên hồ', 'Chụp ảnh street style', 'Giao lưu cộng đồng dev', 'Đang nghĩ ý tưởng mới',
  'Máy tính mới khoe tí', 'Mưa rào bất chợt', 'Bình minh trên phố', 'Đang nghiên cứu AI',
  'Thử nghiệm Docker Swarm', 'Viết documentation', 'Đang refactor code cũ', 'Học thuật toán mới',
  'Setup môi trường dev', 'Tối nay xem concert', 'Đi chợ hoa đêm', 'Nướng bánh cuối tuần',
]

const sampleVideoUrls = [
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4',
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4',
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4',
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4',
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4',
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4',
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4',
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4',
  'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4',
]

const storyUserWeights = (() => {
  const w: number[] = []
  for (let u = 0; u < users.length; u++) {
    const weight = u < 10 ? 5 + Math.floor(Math.random() * 3)
      : u < 40 ? 2 + Math.floor(Math.random() * 2)
      : 1
    for (let c = 0; c < weight; c++) w.push(users[u].id)
  }
  return w
})()

export const stories: Story[] = Array.from({length: 200}, (_, i) => {
  const isVideo = i % 7 === 0
  return {
    id: i + 1,
    userId: storyUserWeights[i % storyUserWeights.length],
    mediaUrl: isVideo
      ? sampleVideoUrls[i % sampleVideoUrls.length]
      : `https://picsum.photos/seed/story${i}/400/700`,
    mediaType: isVideo ? 'VIDEO' : 'IMAGE',
    caption: storyCaptions[i % storyCaptions.length],
    expiresAt: new Date(Date.now() + (72 - (i % 72)) * 3600000).toISOString(),
    viewCount: Math.floor(Math.random() * 500),
    durationMs: isVideo ? 10000 + Math.floor(Math.random() * 15000) : 3000 + Math.floor(Math.random() * 5000),
    createdAt: new Date(Date.now() - (i % 168) * 3600000).toISOString(),
  }
})

// ─── R302: Canvases ───────────────────────────────────────────────
export const canvases: Canvas[] = Array.from({length: 30}, (_, i) => ({
  id: i + 1,
  type: i < 15 ? 'PROFILE' : (i < 25 ? 'COMMUNITY' : 'EVENT'),
  title: i < 15 ? 'Soul Space' : (i < 25 ? `Community Canvas #${i - 14}` : `Event: ${['Tết 2026', 'Halloween 2026', 'Anniversary'][i - 25]}`),
  width: i < 15 ? 200 : 500,
  height: i < 15 ? 200 : 500,
  ownerId: i < 15 ? users[i % 15].id : 0,
  thumbnailUrl: i < 10 ? `https://picsum.photos/seed/canvas${i}/200/200` : null,
  isActive: i < 15,
  isEquipped: i < 10,
  startsAt: i >= 25 ? new Date(Date.now() - (i-25) * 86400000).toISOString() : null,
  endsAt: i >= 25 ? new Date(Date.now() + (3-(i-25)) * 86400000).toISOString() : null,
  createdAt: new Date(Date.now() - i * 86400000).toISOString(),
}))

export const canvasStrokes: CanvasStroke[] = Array.from({length: 200}, (_, i) => ({
  id: i + 1,
  canvasId: (i % 5) + 1,
  userId: users[i % 20].id,
  x: Math.floor(Math.random() * 180) + 10,
  y: Math.floor(Math.random() * 180) + 10,
  color: ['#0ea5e9', '#ef4444', '#10b981', '#f59e0b', '#8b5cf6', '#ec4899', '#ffffff'][i % 7],
  brushSize: [1, 3, 5][i % 3],
  createdAt: new Date(Date.now() - i * 5000).toISOString(),
}))

// ─── R303: Playlists ──────────────────────────────────────────────
export const playlists: Playlist[] = Array.from({length: 20}, (_, i) => ({
  id: i + 1,
  ownerId: users[i % 20].id,
  title: ['Code Flow', 'Chill Vibes', 'Focus Mode', 'Late Night Coding', 'Road Trip'][i % 5],
  isActive: i < 5,
  isPublic: true,
  songCount: 0,
  createdAt: new Date(Date.now() - i * 86400000).toISOString(),
  updatedAt: new Date(Date.now() - i * 3600000).toISOString(),
}))

const songTitles = [
  'Coding Session', 'Midnight Dreams', 'Sunset Boulevard', 'Neon Lights',
  'Ocean Waves', 'Digital Love', 'Starlight', 'Urban Beat',
  'Forest Walk', 'Pixel Rain', 'Cosmic Drift', 'Electric Feel',
  'Golden Hour', 'Deep Focus', 'Summer Nights', 'Lunar Phase',
  'Crystal Clear', 'Firefly', 'Thunderstorm', 'Velvet Sky'
]
const songArtists = ['Lofi Artist', 'Chill Beats', 'Synthwave Pro', 'Ambient Flow', 'Electronic Soul']

export const playlistSongs: PlaylistSong[] = Array.from({length: 100}, (_, i) => ({
  id: i + 1,
  playlistId: (i % 5) + 1,
  addedBy: users[i % 10].id,
  title: songTitles[i % 20],
  artist: songArtists[i % 5],
  source: 'url',
  sourceId: '',
  sourceUrl: '',
  audioUrl: `https://www.soundhelix.com/examples/mp3/SoundHelix-Song-${(i % 16) + 1}.mp3`,
  thumbnailUrl: `https://picsum.photos/seed/music${i}/100/100`,
  durationSec: 180 + Math.floor(Math.random() * 180),
  sortOrder: i % 20,
  voteCount: Math.floor(Math.random() * 20) - 5,
  isApproved: true,
  createdAt: new Date(Date.now() - i * 7200000).toISOString(),
}))

// Update songCount for each playlist
playlists.forEach(p => {
  p.songCount = playlistSongs.filter(s => s.playlistId === p.id).length
})

// ─── Music Comments & Reactions ────────────────────────────────
export const musicReactionTypes = ['LIKE', 'LOVE', 'HAHA', 'FIRE', 'SAD']

export const musicComments: MusicComment[] = Array.from({length: 200}, (_, i) => ({
  id: i + 1,
  songId: (i % 100) + 1,
  userId: users[i % 10].id,
  content: [
    'Hay quá!', 'Chill thật sự', 'Bài này hay lắm', 'Tuyệt vời',
    'Nghe hoài không chán', 'Giai điệu đỉnh', 'Thư giãn quá',
    'Nhạc nền chill', 'Cực phẩm!', 'Mê bài này'
  ][i % 10],
  parentId: null,
  createdAt: new Date(Date.now() - i * 3600000).toISOString(),
}))

// Add nested replies
musicComments.slice(0, 50).forEach((c, i) => {
  if (i % 3 === 0) {
    musicComments.push({
      id: musicComments.length + 1,
      songId: c.songId,
      userId: users[(i + 5) % 10].id,
      content: ['Đồng ý!', 'Mình cũng thấy vậy', 'Hay thật', 'Chuẩn luôn'][i % 4],
      parentId: c.id,
      createdAt: new Date(Date.now() - i * 1800000).toISOString(),
    })
  }
})

export const musicReactions: MusicReaction[] = Array.from({length: 500}, (_, i) => ({
  id: i + 1,
  songId: (i % 100) + 1,
  userId: users[i % 10].id,
  type: musicReactionTypes[i % musicReactionTypes.length],
  createdAt: new Date(Date.now() - i * 3600000).toISOString(),
}))

// ─── R104: Quests ─────────────────────────────────────────────────
export const quests: Quest[] = [
  {id: 1, type: 'DAILY', title: 'Viết 1 blog', description: 'Viết một bài blog mới hôm nay', conditions: JSON.stringify({action: 'WRITE_BLOG', count: 1}), rewards: JSON.stringify({exp: 50, coins: 30}), isActive: true, createdAt: new Date().toISOString()},
  {id: 2, type: 'DAILY', title: 'Đọc 3 blogs', description: 'Đọc 3 bài viết của người khác', conditions: JSON.stringify({action: 'READ_BLOG', count: 3}), rewards: JSON.stringify({exp: 30, coins: 20}), isActive: true, createdAt: new Date().toISOString()},
  {id: 3, type: 'DAILY', title: 'Reaction 5 bài', description: 'Thả reaction 5 bài viết', conditions: JSON.stringify({action: 'REACT', count: 5}), rewards: JSON.stringify({exp: 40, coins: 25}), isActive: true, createdAt: new Date().toISOString()},
  {id: 4, type: 'DAILY', title: 'Viết 1 status', description: 'Đăng một status mới', conditions: JSON.stringify({action: 'WRITE_STATUS', count: 1}), rewards: JSON.stringify({exp: 20, coins: 15}), isActive: true, createdAt: new Date().toISOString()},
  {id: 5, type: 'WEEKLY', title: 'Viết 3 blogs', description: 'Viết 3 bài trong tuần', conditions: JSON.stringify({action: 'WRITE_BLOG', count: 3}), rewards: JSON.stringify({exp: 200, gems: 50}), isActive: true, createdAt: new Date().toISOString()},
]

export const userQuests: UserQuest[] = Array.from({length: 100}, (_, i) => ({
  id: i + 1,
  userId: users[i % 10].id,
  questId: (i % 5) + 1,
  progress: Math.floor(Math.random() * 3),
  target: [(i % 5) + 1, 3, 5, 1, 3][i % 5],
  status: (['IN_PROGRESS', 'COMPLETED', 'CLAIMED'] as const)[i % 3],
  claimedAt: i % 3 === 2 ? new Date().toISOString() : null,
  expiresAt: new Date(Date.now() + 86400000).toISOString(),
  createdAt: new Date().toISOString(),
}))

// ─── R110: Blind Challenge ────────────────────────────────────────
export const blindChallenges: BlindChallenge[] = [
  {id: 1, date: new Date().toISOString().split('T')[0], topicId: 1, topicHint: 'Một ngôn ngữ lập trình', options: JSON.stringify([{id: 1, name: 'Rust'}, {id: 2, name: 'Go'}, {id: 3, name: 'TypeScript'}, {id: 4, name: 'Python'}, {id: 5, name: 'Java'}, {id: 6, name: 'C++'}, {id: 7, name: 'Kotlin'}, {id: 8, name: 'Swift'}, {id: 9, name: 'Ruby'}, {id: 10, name: 'PHP'}]), revealed: false, createdAt: new Date().toISOString()},
]

export const blindChallengeGuesses: BlindChallengeGuess[] = [
  {id: 1, challengeId: 1, userId: 1, guessedTopicId: 1, isCorrect: null, createdAt: new Date().toISOString()},
]

// ─── R103: Skill Trees ────────────────────────────────────────────
export const skillTrees: SkillTree[] = [
  {id: 1, categoryId: 1, name: 'Upload ảnh 10MB', description: 'Mở khóa khả năng upload ảnh dung lượng lớn', perkType: 'UPLOAD_LIMIT', perkValue: JSON.stringify({maxSize: 10}), pointsRequired: 100, sortOrder: 1, createdAt: new Date().toISOString()},
  {id: 2, categoryId: 1, name: 'Scheduled Post', description: 'Lên lịch đăng bài', perkType: 'SCHEDULE', perkValue: JSON.stringify({}), pointsRequired: 300, sortOrder: 2, createdAt: new Date().toISOString()},
  {id: 3, categoryId: 1, name: 'Analytics Pro', description: 'Xem thống kê chi tiết', perkType: 'ANALYTICS', perkValue: JSON.stringify({}), pointsRequired: 500, sortOrder: 3, createdAt: new Date().toISOString()},
  {id: 4, categoryId: 1, name: 'Custom Domain', description: 'Sử dụng domain riêng', perkType: 'CUSTOM_DOMAIN', perkValue: JSON.stringify({}), pointsRequired: 1000, sortOrder: 4, createdAt: new Date().toISOString()},
]

export const userSkillProgress: UserSkillProgress[] = Array.from({length: 50}, (_, i) => ({
  id: i + 1,
  userId: users[i % 20].id,
  categoryId: (i % 5) + 1,
  totalPoints: Math.floor(Math.random() * 500),
}))

export const userSkillUnlocks: UserSkillUnlock[] = Array.from({length: 30}, (_, i) => ({
  id: i + 1,
  userId: users[i % 15].id,
  skillId: (i % 4) + 1,
  unlockedAt: new Date(Date.now() - i * 86400000).toISOString(),
}))

// ===== PROFILE VIEWS =====
export const profileViews: ProfileView[] = Array.from({length: 500}, (_, i) => ({
  id: i + 1,
  userId: users[i % 100].id,
  viewerId: users[(i + 30) % 100].id,
  viewedAt: new Date(Date.now() - i * 3600000).toISOString(),
}))

export const reputationHistory: ReputationHistory[] = Array.from({length: 30}, (_, i) => ({
  date: new Date(Date.now() - (29 - i) * 86400000).toISOString().split('T')[0],
  points: Math.floor(Math.random() * 100 + 10),
}))
