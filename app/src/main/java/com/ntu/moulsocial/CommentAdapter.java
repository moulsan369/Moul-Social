

public class HomeFragment extends Fragment implements PostAdapter.OnPostInteractionListener {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        // Initialize postList with your data

        postAdapter = new PostAdapter(postList, this);
        recyclerViewPosts.setAdapter(postAdapter);

        return view;
    }

    @Override
    public void onLikeClicked(Post post, int position) {
        // Handle like action, e.g., update server or local database
    }

    @Override
    public void onCommentClicked(Post post, int position) {
        // Handle comment action, e.g., show comment dialog or navigate to comment screen
    }

    @Override
    public void onShareClicked(Post post, int position) {
        // Handle share action, e.g., share post via intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
